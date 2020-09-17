package com.example.tvmusicplayer.playlist

import com.example.tvmusicplayer.base.BasePresenter
import com.example.tvmusicplayer.base.BaseView
import com.example.tvmusicplayer.bean.PlayList

/**
 * 获取用户歌单的Contract.
 * */
interface UserPlayListContract {
    interface Presenter : BasePresenter {
        fun getUserPlayList(uid: Long)
    }

    interface OnView : BaseView<Presenter> {
        fun getUserPlayListSuccess(playLists: MutableList<PlayList>)
    }
}