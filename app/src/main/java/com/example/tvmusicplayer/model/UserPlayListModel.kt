package com.example.tvmusicplayer.model

import com.example.tvmusicplayer.base.BaseModelListener
import com.example.tvmusicplayer.bean.PlayList

interface UserPlayListModel {
    fun getUserPlayList(uid: Long,listener : OnListener)
    
    interface OnListener : BaseModelListener{
        fun getUserPlayListSuccess(list : MutableList<PlayList>)
    }
}