package com.example.data.observableApi

import com.example.repository.bean.UserJson
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ObservableLoginApi {
    /**
     * 登陆接口.
     * */
    @GET("/login/cellphone")
    fun login(@Query("phone") username: String, @Query("password") password: String):
            Observable<UserJson>

    /**
     * 获取登陆状态接口.
     * */
    @GET("/login/status")
    fun getLoginStatus() : Observable<UserJson>
    
    @GET("/logout")
    fun logout() : Observable<Unit>
    
    @GET("/login/refresh")
    fun refreshLogin() : Observable<Unit>
}