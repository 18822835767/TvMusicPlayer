package com.example.repository.observableApi

import com.example.repository.GsonBean.UserJson
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ObservableLoginApi {
    /**
     * 登陆接口.
     * */
    @GET("/login/cellphone")
    fun login(@Query("phone") phone: String, @Query("password") password: String):
            Observable<UserJson>


}