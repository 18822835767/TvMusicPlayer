package com.example.data.observableApi

import com.example.repository.bean.*
import io.reactivex.Observable
import io.reactivex.Observer
import retrofit2.http.GET
import retrofit2.http.Query

interface ObservableMusicApi {
    @GET("/user/playlist")
    fun getUserPlayList(@Query("uid")uid : Long) : Observable<UserPlayListJson>

    @GET("/playlist/detail")
    fun getSongListDetail(@Query("id")id: Long) : Observable<SongIdsJson>

    @GET("/song/url")
    fun getSongsPlay(@Query("id")ids: String) : Observable<SongPlayJson>

    @GET("/song/detail")
    fun getSongsDetail(@Query("ids")ids: String) : Observable<SongDetailJson>
    
    @GET("/song/url")
    fun getSongPlay(@Query("id")id : Long) : Observable<SongPlayJson>

    @GET("/lyric")
    fun getSongLyric(@Query("id")id: Long) : Observable<LyricJson>
}