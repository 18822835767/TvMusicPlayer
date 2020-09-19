package com.example.tvmusicplayer.model.impl

import com.example.data.DataUtil
import com.example.repository.RequestCallBack
import com.example.repository.bean.UserJson
import com.example.tvmusicplayer.bean.User
import com.example.tvmusicplayer.manager.LoginStatusManager
import com.example.tvmusicplayer.model.LoginModel

class LoginModelImpl : LoginModel {

    override fun login(username: String, password: String, listener: LoginModel.OnListener) {
        DataUtil.clientLoginApi.login(username, password, object :
            RequestCallBack<UserJson> {
            override fun callback(data: UserJson) {
                if (data.profile == null) {
                    //说明登录失败，账号密码错误...
                    listener.loginFailure()
                } else {
                    val user = User(data.profile?.userId ?: -1, data.profile?.nickname)
                    //标记已经登陆成功
                    LoginStatusManager.alreadyLogin = true
                    LoginStatusManager.user = user
                    //登录成功
                    listener.loginSuccess(user)
                }
            }

            override fun error(errorMsg: String) {
                listener.error(errorMsg)
            }
        })
    }

}