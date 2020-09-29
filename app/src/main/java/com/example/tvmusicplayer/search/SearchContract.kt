package com.example.tvmusicplayer.search

import com.example.tvmusicplayer.base.BasePresenter
import com.example.tvmusicplayer.base.BaseView

interface SearchContract {
    interface Presenter : BasePresenter{
        
    }
    
    interface OnView : BaseView<Presenter>{
        
    }
}