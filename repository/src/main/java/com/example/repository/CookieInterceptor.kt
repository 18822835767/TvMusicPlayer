package com.example.repository

import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp的拦截器，用于拦截Cookie.
 * */
class CookieInterceptor : Interceptor{
    
    override fun intercept(chain: Interceptor.Chain): Response {
        
        TODO("Not yet implemented")
    }
}