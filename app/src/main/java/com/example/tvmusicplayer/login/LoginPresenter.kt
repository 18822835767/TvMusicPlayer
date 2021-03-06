package com.example.tvmusicplayer.login

import com.example.tvmusicplayer.bean.User
import com.example.tvmusicplayer.model.LoginModel
import com.example.tvmusicplayer.model.impl.LoginModelImpl

class LoginPresenter(var onView : LoginContract.OnView) : LoginContract.Presenter,LoginModel.OnListener {
    
    private val loginModel : LoginModel
    
    init {
        onView.setPresenter(this)    
        loginModel = LoginModelImpl()
    }
    
    override fun login(username: String, password: String) {
        onView.showLoading()
        loginModel.login(username,password,this)
    }

    override fun start() {
    }

    override fun loginSuccess(user: User) {
        onView.hideLoading()
        onView.loginSuccess(user)
    }

    override fun loginFailure() {
        onView.hideLoading()
        onView.loginFailure()
    }
    
    override fun error(msg: String) {
        onView.hideLoading()
        onView.showError(msg)
    }
    
}