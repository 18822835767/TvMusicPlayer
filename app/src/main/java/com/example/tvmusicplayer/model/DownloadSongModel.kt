package com.example.tvmusicplayer.model

import com.example.tvmusicplayer.base.BaseModelListener
import com.example.tvmusicplayer.bean.Song

interface DownloadSongModel {
    fun insert(song : Song)
    
    fun delete(songId : Long)
    
    fun querySongPath(songId : Long, listener : OnListener)
    
    interface OnListener{
        fun querySongPathSuccess(path : String?)
    }
}