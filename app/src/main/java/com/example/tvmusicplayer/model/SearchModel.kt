package com.example.tvmusicplayer.model

import com.example.tvmusicplayer.base.BaseModelListener
import com.example.tvmusicplayer.bean.Song

interface SearchModel {
    fun searchOrLoadMoreSongs(limit: Int, offset: Int, type: Int, keyword: String, listener : OnListener)
    
    interface OnListener : BaseModelListener{
        fun searchSuccess(list : MutableList<Song>,songCount : Int)
        fun loadMoreSuccess(list : MutableList<Song>, songCount : Int)
    }
}