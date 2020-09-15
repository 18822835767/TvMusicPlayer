package com.example.tvmusicplayer.model

import com.example.tvmusicplayer.bean.User

interface LoginModel{
    
    fun login(username : String,password : String,listener : OnListener)
    
    interface OnListener{
        fun loginSuccess(user : User)
    }
}