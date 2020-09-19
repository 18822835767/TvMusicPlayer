package com.example.data.observableApi

import com.example.repository.bean.UserPlayListJson
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ObservableMusicApi {
    @GET("/user/playlist")
    fun getUserPlayList(@Query("uid")uid : Long) : Observable<UserPlayListJson>
}