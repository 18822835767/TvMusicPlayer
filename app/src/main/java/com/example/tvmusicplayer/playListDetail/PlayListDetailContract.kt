package com.example.tvmusicplayer.playListDetail

import com.example.tvmusicplayer.base.BasePresenter
import com.example.tvmusicplayer.base.BaseView
import com.example.tvmusicplayer.bean.Song

interface PlayListDetailContract {
    interface Presenter : BasePresenter{
        fun getPlayListDetail(id : Long)
    }
    
    interface OnView : BaseView<Presenter>{
        fun getPlayListDetailSuccess(list: MutableList<Song>)
    }
}