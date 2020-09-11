package com.example.repository

import retrofit2.http.GET
import rx.Observable

interface MusicApiService {
    @GET("/logout")
    fun logout()
    
}