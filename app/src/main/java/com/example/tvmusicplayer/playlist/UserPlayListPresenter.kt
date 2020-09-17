package com.example.tvmusicplayer.playlist

import com.example.tvmusicplayer.bean.PlayList
import com.example.tvmusicplayer.model.UserPlayListModel
import com.example.tvmusicplayer.model.impl.UserPlayListModelImpl

class UserPlayListPresenter(var onView : UserPlayListContract.OnView) : UserPlayListContract.Presenter
    ,UserPlayListModel.OnListener{
    
    private val model : UserPlayListModel
    
    init{
        onView.setPresenter(this)
        model = UserPlayListModelImpl()
    }
    
    override fun getUserPlayList(uid: Long) {
        model.getUserPlayList(uid,this)
    }

    override fun start() {
    }

    override fun getUserPlayListSuccess(list: MutableList<PlayList>) {
        onView.hideLoading()
        onView.getUserPlayListSuccess(list)
    }

    override fun error(msg: String) {
        onView.hideLoading()
        onView.showError(msg)
    }
}