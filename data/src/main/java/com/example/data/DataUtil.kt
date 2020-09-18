package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.data.api.ClientLoginApi
import com.example.data.api.ClientMusicApi
import com.example.data.clientApiImpl.ClientLoginApiImpl
import com.example.data.clientApiImpl.ClientMusicApiImpl
import com.example.data.observableApi.ObservableLoginApi
import com.example.data.observableApi.ObservableMusicApi
import com.example.data.util.Constant
import com.example.data.util.ContextProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory


object DataUtil{

    private var client: OkHttpClient
    private var retrofit: Retrofit
    internal var observableLoginApi: ObservableLoginApi
    internal var observableMusicApi: ObservableMusicApi
    internal var sharedPreferences : SharedPreferences
    private var cookies = HashMap<String,String>()

    /**
     * 暴露给用户使用的...
     * */
    val clientLoginApi : ClientLoginApi =
        ClientLoginApiImpl()
    val clientMusicApi : ClientMusicApi=
        ClientMusicApiImpl()

    init {
        //初始化SharedPreferences
        sharedPreferences = ContextProvider.applicationContext
            .getSharedPreferences("Cookie_Pre",Context.MODE_PRIVATE)
        
        client = OkHttpClient.Builder()
            .connectTimeout(Constant.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(Constant.READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(Constant.WRITE_TIME_OUT, TimeUnit.SECONDS)
//            .addInterceptor(object : Interceptor{
//                override fun intercept(chain: Interceptor.Chain): Response {
//                    //获取请求连接
//                    val originalRequest = chain.request()
//                    //获取url的主机地址
//                    val hostString = originalRequest.url.host
//                    
//                    //如果当前内存里不含有hostString对应的cookie
//                    if(!cookies.containsKey(hostString)){
//                        //从磁盘里面获取cookie
//                        val spCookie = sharedPreferences.getString(hostString,"")
//                        //如果cookie不为空，长度不为0
//                        if(!TextUtils.isEmpty(spCookie)){
//                            spCookie?.let {
//                                //将cookie放到内存中
//                                cookies.put(hostString,it)
//                            }
//                        }
//                    }
//                    
//                    //获取内存中的cookie
//                    val memoryCookie = cookies.get(hostString)
//                    //设置Request数据
//                    
////                    
//                }
//
//            })
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
    
    
}