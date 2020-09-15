package com.example.repository.clientApi

import com.example.repository.GsonBean.UserJson
import com.example.repository.callback.CallBack

/**
 * 暴露给用户使用的Api.
 * */
interface ClientLoginApi{
    fun login(phone: String, password: String,callback : CallBack<UserJson>)
}