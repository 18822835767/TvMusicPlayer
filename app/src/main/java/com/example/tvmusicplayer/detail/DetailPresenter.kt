package com.example.tvmusicplayer.detail

import com.example.tvmusicplayer.model.SongInfoModel
import com.example.tvmusicplayer.model.impl.SongInfoModelImpl

class DetailPresenter(var onView : DetailContract.OnView) : DetailContract.Presenter,
    SongInfoModel.OnLyricsListener{
    
    private var model : SongInfoModel
    
    init {
        onView.setPresenter(this)
        model = SongInfoModelImpl()
    }
    
    override fun getSongLyrics(id: Long) {
        model.getSongLyrics(id,this)
    }

    override fun start() {
    }

    override fun getLyricsSuccess(lyricsText: String) {
        onView.getLyricsSuccess(lyricsText)
    }

    override fun error(msg: String) {
        onView.showError(msg)
    }

}