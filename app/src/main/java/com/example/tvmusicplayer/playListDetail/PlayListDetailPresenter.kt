package com.example.tvmusicplayer.playListDetail

import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.model.PlayListDetailModel
import com.example.tvmusicplayer.model.impl.PlayListDetailModelImpl

class PlayListDetailPresenter(val onView: PlayListDetailContract.OnView) :
    PlayListDetailContract.Presenter
    , PlayListDetailModel.OnListener {

    private val model: PlayListDetailModel

    init {
        onView.setPresenter(this)
        model = PlayListDetailModelImpl()
    }

    override fun getPlayListDetail(id: Long) {
        onView.showLoading()
        model.getPlayListDetail(id,this)
    }

    override fun start() {
    }

    override fun getPlayListDetailSuccess(list: MutableList<Song>) {
        onView.hideLoading()
        onView.getPlayListDetailSuccess(list)
    }

    override fun error(msg: String) {
        onView.hideLoading()
        onView.showError(msg)
    }

}