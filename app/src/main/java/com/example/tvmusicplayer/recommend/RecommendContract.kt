package com.example.tvmusicplayer.recommend

import com.example.tvmusicplayer.base.BasePresenter
import com.example.tvmusicplayer.base.BaseView
import com.example.tvmusicplayer.bean.PlayList
import com.example.tvmusicplayer.bean.Song

interface RecommendContract {
    interface Presenter : BasePresenter{
        fun getBanner(type : Int)
        fun getRecommendPlayList(limit : Int)
        fun getRecommendNewSong()
    }
    
    interface OnView : BaseView<Presenter>{
        fun getBannerSuccess(list : MutableList<String>)
        fun getRecommendPlayListSuccess(list : MutableList<PlayList>)
        fun getRecommendNewSongSuccess(list : MutableList<Song>)
    }
}