package com.example.tvmusicplayer.model

import com.example.tvmusicplayer.base.BaseModelListener
import com.example.tvmusicplayer.bean.Song

interface LocalModel {
    /**
     * 获取本地歌曲.
     * */
    fun getLocalSongs(listener: OnListener)

    interface OnListener : BaseModelListener {
        fun getLocalSongsSuccess(list: MutableList<Song>)
    }
}