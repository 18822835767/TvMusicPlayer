package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.example.repository.api.ClientLoginApi
import com.example.repository.api.ClientMusicApi
import com.example.data.clientApiImpl.ClientLoginApiImpl
import com.example.data.clientApiImpl.ClientMusicApiImpl
import com.example.data.observableApi.ObservableLoginApi
import com.example.data.observableApi.ObservableMusicApi
import com.example.data.util.Constant
import com.example.data.util.ContextProvider
import com.example.data.util.LogUtil
import com.example.repository.RequestCallBack
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
    internal var observableLoginApi: ObservableLoginApi
    internal var observableMusicApi: ObservableMusicApi
    internal var sharedPreferences: SharedPreferences
    private var cookies = HashMap<String, String>()

    /**
     * 暴露给用户使用的...
     * */
    val clientLoginApi: ClientLoginApi =
        ClientLoginApiImpl()
    val clientMusicApi: ClientMusicApi =
        ClientMusicApiImpl()

    init {
        //初始化SharedPreferences
        sharedPreferences = ContextProvider.applicationContext
            .getSharedPreferences("Cookie_Pre", Context.MODE_PRIVATE)

        client = OkHttpClient.Builder()
            .connectTimeout(Constant.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(Constant.READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(Constant.WRITE_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    //获取请求连接
                    val originalRequest = chain.request()
                    //获取url的主机地址
                    val hostString = originalRequest.url.host

                    //如果当前内存里不含有hostString对应的cookie
                    if (!cookies.containsKey(hostString)) {
                        //从磁盘里面获取cookie
                        val spCookie = sharedPreferences.getString(hostString, "")
                        //如果cookie不为空，长度不为0
                        if (!TextUtils.isEmpty(spCookie)) {
                            spCookie?.let {
                                //将cookie放到内存中
                                cookies.put(hostString, it)
                            }
                        }
                    }

                    //获取内存中的cookie
                    val memoryCookie = cookies.get(hostString)
                    //拦截网络请求数据
                    val request = originalRequest.newBuilder()
                        //设置请求头cookie
                        .addHeader("Cookie", memoryCookie ?: "")
                        .build()

                    //拦截返回的数据
                    val originalResponse = chain.proceed(request)
                    //判断请求头里面是否有Set-Cookie值，更新Cookie
                    if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                        //字符串集
                        val stringBuilder = StringBuilder()
                        originalResponse.headers("Set-Cookie").forEach {
                            stringBuilder.append(it)
                            stringBuilder.append(";")
                        }
                        //拼接字符串
                        val cookie = stringBuilder.toString()

                        //更新内存中Cookie值
                        cookies.put(hostString, cookie)
                        //存储到本地磁盘中
                        val editor = sharedPreferences.edit()
                        editor.putString(hostString, cookie)
                        editor.apply()
                        LogUtil.d(TAG, "Set-Cookie->cookies: $cookie host: $hostString")
                    }
                    return originalResponse
                }

            })
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

        //获取ObservableApi
        observableLoginApi = retrofit.create(ObservableLoginApi::class.java)
        observableMusicApi = retrofit.create(ObservableMusicApi::class.java)
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
                callback.callback(response.body.toString())
            }

        })
    }

}