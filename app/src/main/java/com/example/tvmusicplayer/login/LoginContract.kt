package com.example.tvmusicplayer.login

import com.example.tvmusicplayer.base.BasePresenter
import com.example.tvmusicplayer.base.BaseView
import com.example.tvmusicplayer.bean.User

interface LoginContract {
    interface Presenter : BasePresenter{
        fun login(username : String,password : String)
    }
    
    interface OnView : BaseView<Presenter>{
        fun showSuccess(user : User)
    }
}