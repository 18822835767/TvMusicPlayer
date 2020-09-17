package com.example.tvmusicplayer.model

import com.example.tvmusicplayer.base.BaseModelListener
import com.example.tvmusicplayer.bean.User

interface LoginModel{
    
    fun login(username : String,password : String,listener : OnListener)
    
    interface OnListener : BaseModelListener{
        fun loginSuccess(user : User)
        fun loginFailure()
    }
}