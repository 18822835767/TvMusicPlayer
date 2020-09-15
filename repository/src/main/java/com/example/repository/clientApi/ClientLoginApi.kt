package com.example.repository.clientApi

import com.example.repository.GsonBean.UserJson

/**
 * 暴露给用户使用的Api.
 * */
interface ClientLoginApi{
    fun login(phone: String, password: String): UserJson
}