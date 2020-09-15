package com.example.tvmusicplayer.login

class LoginPresenter(var onView : LoginContract.OnView) : LoginContract.Presenter {
    
    init {
        onView.setPresenter(this)    
    }
    
    override fun login(username: String, password: String) {
        
    }

    override fun start() {
    }

}