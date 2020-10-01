package com.example.tvmusicplayer.detail

import com.example.tvmusicplayer.base.BasePresenter
import com.example.tvmusicplayer.base.BaseView

class DetailContract {
    interface Presenter : BasePresenter{
        fun getSongLyrics(id: Long)
    }
    
    interface OnView : BaseView<Presenter>{
        fun getLyricsSuccess(lyricsText : String)
    }
}
