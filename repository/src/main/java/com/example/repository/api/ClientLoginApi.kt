package com.example.repository.clientApi

import com.example.repository.GsonBean.UserJson
import com.example.repository.callback.RequestCallBack

/**
 * 暴露给用户使用的Api.
 * */
interface ClientLoginApi{
    fun login(username: String, password: String, callback : RequestCallBack<UserJson>)
    fun refreshLogin()
    fun getLoginStatus(callback : RequestCallBack<UserJson>)
    fun logout()
}