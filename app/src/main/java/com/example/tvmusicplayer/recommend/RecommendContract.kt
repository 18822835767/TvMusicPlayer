package com.example.tvmusicplayer.recommend

import com.example.tvmusicplayer.base.BasePresenter
import com.example.tvmusicplayer.base.BaseView

interface RecommendContract {
    interface Presenter : BasePresenter{
        fun getBanner(type : Int)
    }
    
    interface OnView : BaseView<Presenter>{
        fun getBannerSuccess(list : MutableList<String>)
    }
}