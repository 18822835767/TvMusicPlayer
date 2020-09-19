package com.example.tvmusicplayer.home

import com.example.tvmusicplayer.base.BasePresenter
import com.example.tvmusicplayer.base.BaseView

interface HomeContract {
    interface Presenter : BasePresenter{
        fun logout()
    }
    
    interface OnView : BaseView<Presenter>{
        
    }
}