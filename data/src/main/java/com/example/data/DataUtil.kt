package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.data.clientApiImpl.ClientImageApiImpl
import com.example.repository.api.ClientLoginApi
import com.example.repository.api.ClientMusicApi
import com.example.data.clientApiImpl.ClientLoginApiImpl
import com.example.data.clientApiImpl.ClientMusicApiImpl
import com.example.data.clientApiImpl.ClientSearchApiImpl
import com.example.data.observableApi.ObservableImageApi
import com.example.data.observableApi.ObservableLoginApi
import com.example.data.observableApi.ObservableMusicApi
import com.example.data.observableApi.ObservableSearchApi
import com.example.data.util.Constant
import com.example.data.util.ContextProvider
import com.example.data.util.LogUtil
import com.example.repository.RequestCallBack
import com.example.repository.api.ClientImageApi
import com.example.repository.api.ClientSearchApi
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.StringBuilder


object DataUtil {

    private const val TAG = "DataUtil"
    private var client: OkHttpClient
    private var retrofit: Retrofit
    private var clientBuilder: OkHttpClient.Builder
    internal var observableLoginApi: ObservableLoginApi
    internal var observableMusicApi: ObservableMusicApi
    internal var observableSearchApi : ObservableSearchApi
    internal var observableImageApi : ObservableImageApi
    internal var sharedPreferences: SharedPreferences? = null
    private var cookies = HashMap<String, String>()

    /**
     * 暴露给用户使用的...
     * */
    val clientLoginApi: ClientLoginApi =
        ClientLoginApiImpl()
    val clientMusicApi: ClientMusicApi =
        ClientMusicApiImpl()
    val clientSearchApi : ClientSearchApi = 
        ClientSearchApiImpl()
    val clientImageApi : ClientImageApi = 
        ClientImageApiImpl()

    init {
//        //初始化SharedPreferences
//        sharedPreferences = ContextProvider.applicationContext
//            .getSharedPreferences("Cookie_Pre", Context.MODE_PRIVATE)

        clientBuilder = OkHttpClient.Builder()
            .connectTimeout(Constant.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(Constant.READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(Constant.WRITE_TIME_OUT, TimeUnit.SECONDS)
        
        client = clientBuilder.build()

        //context不为空
        ContextProvider.applicationContext?.let {
            sharedPreferences = it.getSharedPreferences("Cookie_Pre", Context.MODE_PRIVATE)
            sharedPreferences?.let { sp->
                client = clientBuilder.addInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        //获取请求连接
                        val originalRequest = chain.request()
                        //获取url的主机地址
                        val hostString = originalRequest.url.host

                        //如果当前内存里不含有hostString对应的cookie
                        if (!cookies.containsKey(hostString)) {
                            //从磁盘里面获取cookie
                            val spCookie = sp.getString(hostString, "")
                            //如果cookie不为空，长度不为0
                            if (spCookie != null && spCookie != "") {
                                //将cookie放到内存中
                                cookies[hostString] = spCookie
                            }
                        }

                        //获取内存中的cookie
                        val memoryCookie = cookies[hostString]
                        //拦截网络请求数据
                        val request = originalRequest.newBuilder()
                            //设置请求头cookie
                            .addHeader("Cookie", memoryCookie ?: "")
                            .build()

                        LogUtil.d(TAG, "CookieHeader: $memoryCookie")
                        
                        //拦截返回的数据
                        val originalResponse = chain.proceed(request)
                        //判断请求头里面是否有Set-Cookie值，更新Cookie
                        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
                            //字符串集
                            val stringBuilder = StringBuilder()
                            originalResponse.headers("Set-Cookie").forEach {str->
                                stringBuilder.append(str)
                                stringBuilder.append(";")
                            }
                            //拼接字符串
                            val cookie = stringBuilder.toString()

                            LogUtil.d(TAG,"返回的cookie:${cookie}")
                            //如果获取的cookie不是以NMTID开头，这里加上是因为获取用户歌单时(or 获取登陆状态)会返回一个cookie
                            //但是这cookie是不需要的
                            if(!cookie.startsWith("NMTID",true)){
                                //更新内存中Cookie值
                                cookies[hostString] = cookie
                                //存储到本地磁盘中
                                val editor = sp.edit()
                                editor.putString(hostString, cookie)
                                editor.apply()
                                LogUtil.d(TAG, "Set-Cookie->cookies: $cookie host: $hostString")
                            }
                        }
                        return originalResponse
                    }

                }).build()
            }
        }
        
        retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

        //获取ObservableApi
        observableLoginApi = retrofit.create(ObservableLoginApi::class.java)
        observableMusicApi = retrofit.create(ObservableMusicApi::class.java)
        observableSearchApi = retrofit.create(ObservableSearchApi::class.java)
        observableImageApi = retrofit.create(ObservableImageApi::class.java)
    }

    /**
     * OkHttp解析，直接返回Json数据.
     * */
    fun getJsonData(url: String, callback: RequestCallBack<String>) {
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.error(e.message ?: "UnKnown_error")
            }

            override fun onResponse(call: Call, response: Response) {
                callback.callback(response.body?.string()?:"")
            }

        })
    }

}