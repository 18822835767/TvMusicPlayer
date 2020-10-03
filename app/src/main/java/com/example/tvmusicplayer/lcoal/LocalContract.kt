package com.example.tvmusicplayer.lcoal

import com.example.tvmusicplayer.base.BasePresenter
import com.example.tvmusicplayer.base.BaseView
import com.example.tvmusicplayer.bean.Song

interface LocalContract {
    interface Presenter : BasePresenter{
        fun getLocalSongs()
    }
    
    interface OnView : BaseView<Presenter>{
        fun getLocalSongsSuccess(songs : MutableList<Song>)
    }
}