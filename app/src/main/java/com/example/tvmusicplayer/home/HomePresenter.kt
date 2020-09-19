package com.example.tvmusicplayer.home

import com.example.tvmusicplayer.model.LoginModel
import com.example.tvmusicplayer.model.impl.LoginModelImpl

class HomePresenter(var onView : HomeContract.OnView) : HomeContract.Presenter{
    
    private val model : LoginModel
    
    init {
        onView.setPresenter(this)
        model = LoginModelImpl()
    }
    
    override fun logout() {
        model.logout()
    }

    override fun start() {
    }

}