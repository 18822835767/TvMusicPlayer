package com.example.repository

import com.example.repository.util.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory


class DataUtil private constructor() {

    private var client : OkHttpClient 
    private var retrofit : Retrofit
    
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
    }
    
    /**
     * 获取单例.
     * */
    fun getInstance(): DataUtil {
        return InstanceHolder.instance
    }

    /**
     * 单例模式，静态内部类构造对象.
     * */
    private class InstanceHolder {
        companion object {
            internal val instance: DataUtil = DataUtil()
        }
    }

}