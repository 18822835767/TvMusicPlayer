package com.example.repository

import com.example.repository.clientApi.ClientLoginApi
import com.example.repository.clientApi.impl.ClientLoginApiImpl
import com.example.repository.clientApi.impl.ClientMusicApiImpl
import com.example.repository.observableApi.ObservableLoginApi
import com.example.repository.observableApi.ObservableMusicApi
import com.example.repository.util.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory


class DataUtil{

    private var client: OkHttpClient
    private var retrofit: Retrofit
    internal var observableLoginApi: ObservableLoginApi
    internal var observableMusicApi: ObservableMusicApi

    /**
     * 暴露给用户使用的...
     * */
    val clientLoginApi = ClientLoginApiImpl()
    val clientMusicApi = ClientMusicApiImpl()

    init {
        client = OkHttpClient.Builder()
            .connectTimeout(Constant.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(Constant.READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(Constant.WRITE_TIME_OUT, TimeUnit.SECONDS)
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