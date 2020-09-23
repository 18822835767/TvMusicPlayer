package com.example.tvmusicplayer.model

import com.example.tvmusicplayer.base.BaseModelListener
import com.example.tvmusicplayer.bean.Song

/**
 * 获取歌单详情，即歌单中的歌曲的接口.
 * */
interface PlayListDetailModel {
    /**
     * 传入歌单id，获取歌单中所有歌曲的歌曲名字、歌手名字等信息.
     * */
    fun getPlayListDetail(id: Long, listener: OnListener)
    
    interface OnListener : BaseModelListener {
        fun getPlayListDetailSuccess(list: MutableList<Song>)
    }
}