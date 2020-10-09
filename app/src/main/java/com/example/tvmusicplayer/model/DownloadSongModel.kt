package com.example.tvmusicplayer.model

import com.example.tvmusicplayer.base.BaseModelListener
import com.example.tvmusicplayer.bean.Song

interface DownloadSongModel {
    fun insert(song : Song)
    
    fun delete(songId : Long)
    
    fun querySong(songId : Long,listener : OnListener)
    
    interface OnListener : BaseModelListener{
        fun querySongSuccess(song : Song)
    }
}