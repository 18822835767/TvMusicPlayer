package com.example.tvmusicplayer

import android.app.Application
import com.example.tvmusicplayer.model.LoginModel
import com.example.tvmusicplayer.model.impl.LoginModelImpl

class MyApplication : Application() {
    private val loginModel: LoginModel = LoginModelImpl()

    override fun onCreate() {
        super.onCreate()

        //获取登陆的信息.
        loginModel.getLoginStatus()
    }
}