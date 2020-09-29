package com.example.tvmusicplayer.search

import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.model.SearchModel
import com.example.tvmusicplayer.model.impl.SearchModelImpl

class SearchPresenter(var onView : SearchContract.OnView) : SearchContract.Presenter,SearchModel.OnListener{
    
    private val model : SearchModel
    
    init {
        onView.setPresenter(this)
        model = SearchModelImpl()
    }
    
    override fun searchSongs(limit: Int, offset: Int, type: Int, keyword: String) {
        
        model.searchSongs(limit,offset,type,keyword,this)
    }

    override fun start() {
    }

    override fun searchSuccess(list: MutableList<Song>) {
        onView.searchSuccess(list)
    }

    override fun error(msg: String) {
        onView.showError(msg)
    }
}