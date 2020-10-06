package com.example.tvmusicplayer.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object DownloadUtil {
    private var client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    

}