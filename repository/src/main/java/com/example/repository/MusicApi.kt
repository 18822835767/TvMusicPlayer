package com.example.repository

import com.example.repository.GsonBean.SongListJson

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