package com.example.data.observableApi

import com.example.repository.bean.BannerJson
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ObservableImageApi {
    @GET("/banner")
    fun getBanners(@Query("type")type : Int) : Observable<BannerJson>
}