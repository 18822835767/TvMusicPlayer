package com.example.tvmusicplayer.lcoal

import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.model.LocalModel
import com.example.tvmusicplayer.model.impl.LocalModelImpl
import com.example.tvmusicplayer.util.LocalSongsUtil

class LocalPresenter(var onView : LocalContract.OnView) : LocalContract.Presenter,LocalModel.OnListener{
    
    private val model : LocalModel
    
    init {
        onView.setPresenter(this)    
        model = LocalModelImpl()
    }
    
    override fun getLocalSongs() {
       model.getLocalSongs(this)
    }

    override fun start() {
    }

    override fun getLocalSongsSuccess(list: MutableList<Song>) {
        onView.getLocalSongsSuccess(list)
    }

    override fun error(msg: String) {
        onView.showError(msg)
    }

}