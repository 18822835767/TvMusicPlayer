package com.example.tvmusicplayer.manager

import com.example.tvmusicplayer.bean.User

/**
 * 用于管理登陆状态.
 * */
object LoginStatusManager {
    var alreadyLogin: Boolean = false
    var user: User? = null
//    var alreadyLogin: Boolean = true
//    var user: User? = User(1333207903,"")
}