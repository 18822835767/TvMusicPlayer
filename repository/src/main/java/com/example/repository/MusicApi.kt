package com.example.repository

import com.example.repository.GsonBean.MusicDetailJson
import com.example.repository.GsonBean.SongListJson
import retrofit2.http.GET
import rx.Observable

/**
 * 这是暴露给用户的接口.
 * */
interface MusicApi {
    /**
     * 根据用户id获取歌单信息.
     * */
    fun getUserSongListJson(id: Long): SongListJson

    /**
     * 根据歌单id获取歌单的歌曲信息.
     * */
    
    
}