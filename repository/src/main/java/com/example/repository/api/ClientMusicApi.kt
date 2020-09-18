package com.example.repository.api

import com.example.repository.GsonBean.SongDetailJson
import com.example.repository.GsonBean.SongPlayJson
import com.example.repository.GsonBean.UserPlayListJson
import com.example.repository.bean.LyricJson
import com.example.repository.bean.SongIdsJson
import com.example.repository.callback.RequestCallBack

/**
 * 暴露给用户使用的，与音乐有关的Api.
 * */
interface ClientMusicApi {
    /**
     * 获取用户的歌单的详细信息，传入用户id.
     * */
    fun getUserPlayList(uid: Long, callBack: RequestCallBack<UserPlayListJson>)

    /**
     * 传入歌单id，获取歌单包含的歌曲.
     * */
    fun getSongListDetail(id: Long, callBack: RequestCallBack<SongIdsJson>)

    /**
     * 传入歌曲的id，获取歌曲详情.
     * */
    fun getSongDetail(id: Long, callBack: RequestCallBack<SongDetailJson>)

    /**
     * 获取歌曲url、码率等信息，传入歌曲的单个id
     * */
    fun getSongPlay(id: Long, callBack: RequestCallBack<SongPlayJson>)

    /**
     * 获取歌曲url、码率等信息，传入歌曲的多个id
     * */
    fun getSongsPlay(ids: String, callBack: RequestCallBack<SongPlayJson>)

    /**
     * 获取歌曲的歌词.
     * */
    fun getSongLyric(id: Long, callBack: RequestCallBack<LyricJson>)

}