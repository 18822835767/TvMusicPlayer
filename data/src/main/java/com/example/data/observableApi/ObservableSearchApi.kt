package com.example.data.observableApi

import com.example.repository.bean.HotList
import com.example.repository.bean.SearchDefaultJson
import com.example.repository.bean.SearchSongJson
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ObservableSearchApi {
    @GET("/search")
    fun searchMusics(@Query("limit")limit : Int, @Query("offset")offset : Int,
                     @Query("type")type : Int, @Query("keywords")keywords : String)
            : Observable<SearchSongJson>

    @GET("/search/default")
    fun getDefaultKeywords() : Observable<SearchDefaultJson>

    @GET("/search/hot/detail")
    fun getHotList() : Observable<HotList>
}