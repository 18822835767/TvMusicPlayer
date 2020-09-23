package com.example.tvmusicplayer.model

import com.example.tvmusicplayer.base.BaseModelListener
import com.example.tvmusicplayer.bean.Song

interface SongInfoModel {
    /**
     * 传入歌曲的id和歌曲的实例，获取歌曲播放的url、size、码率等信息，设置在对象里面.
     * */
    fun getSongPlayInfo(song: Song,listener : OnListener)
    
    interface OnListener : BaseModelListener{
        fun getSongPlayInfoSuccess(song : Song)
    }
}