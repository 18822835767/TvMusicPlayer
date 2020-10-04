package com.example.tvmusicplayer.model

import com.example.tvmusicplayer.base.BaseModelListener

interface RecommendModel {
    fun getBanner(type : Int,listener : OnListener)
    
    interface OnListener : BaseModelListener{
        fun getBannerSuccess(list : MutableList<String>)
    }
}