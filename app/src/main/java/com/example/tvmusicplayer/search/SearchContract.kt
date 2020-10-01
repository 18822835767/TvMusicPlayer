package com.example.tvmusicplayer.search

import com.example.tvmusicplayer.base.BasePresenter
import com.example.tvmusicplayer.base.BaseView
import com.example.tvmusicplayer.bean.Song

interface SearchContract {
    interface Presenter : BasePresenter{
        fun searchSongs(limit : Int,offset : Int,type : Int,keyword : String)
        fun loadMoreSongs(limit : Int, offset : Int, type : Int, keyword : String)
    }
    
    interface OnView : BaseView<Presenter>{
        fun searchSuccess(list : MutableList<Song>,songCount : Int)
        fun loadMoreSuccess(list: MutableList<Song>, songCount: Int)
        
        fun searchError(msg : String)
        fun loadMoreError(msg : String)
    }
}