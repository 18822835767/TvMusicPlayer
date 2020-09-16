package com.example.repository

import com.example.repository.clientApi.ClientLoginApi
import com.example.repository.clientApi.impl.ClientLoginApiImpl
import com.example.repository.observableApi.ObservableLoginApi
import com.example.repository.util.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory


object DataUtil{

    private var client : OkHttpClient 
    private var retrofit : Retrofit
    internal var observableLoginApi : ObservableLoginApi
    val clientLoginApi = ClientLoginApiImpl()
    
    init {
        client = OkHttpClient.Builder()
            .connectTimeout(8000,TimeUnit.MILLISECONDS)
            .readTimeout(8000,TimeUnit.MILLISECONDS)
            .writeTimeout(8000,TimeUnit.MILLISECONDS)
            .build()
        
        retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
        
        //获取ObservableApi
        observableLoginApi = retrofit.create(ObservableLoginApi::class.java)
    }
    

}