package com.example.data.api

import com.example.data.RequestCallBack
import com.example.data.bean.UserJson

/**
 * 暴露给用户使用的Api.
 * */
interface ClientLoginApi{
    fun login(username: String, password: String, callback : RequestCallBack<UserJson>)
//    fun refreshLogin()
//    fun getLoginStatus(callback : RequestCallBack<UserJson>)
//    fun logout()
}