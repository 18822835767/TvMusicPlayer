package com.example.tvmusicplayer.model

import com.example.tvmusicplayer.base.BaseModelListener
import com.example.tvmusicplayer.bean.PlayList
import com.example.tvmusicplayer.bean.Song

interface RecommendModel {
    fun getBanner(type : Int,listener : OnListener)
    fun getRecommendPlayList(limit : Int,listener: OnListener)
    fun getRecommendNewSong(listener: OnListener)
    
    interface OnListener : BaseModelListener{
        fun getBannerSuccess(list : MutableList<String>)
        fun getRecommendPlayListSuccess(list : MutableList<PlayList>)
        fun getRecommendNewSongSuccess(list : MutableList<Song>)
    }
}