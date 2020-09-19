package com.example.tvmusicplayer.model

import com.example.tvmusicplayer.base.BaseModelListener
import com.example.tvmusicplayer.bean.User

interface LoginModel {

    fun login(username: String, password: String, listener: OnListener)

    /**
     * 获取登陆状态，这里不设置回调接口.
     * */
    fun getLoginStatus()

    interface OnListener : BaseModelListener {
        fun loginSuccess(user: User)
        fun loginFailure()
    }
}