package com.example.tvmusicplayer.model.impl

import com.example.repository.DataUtil
import com.example.repository.GsonBean.UserJson
import com.example.repository.callback.CallBack
import com.example.tvmusicplayer.bean.User
import com.example.tvmusicplayer.model.LoginModel

class LoginModelImpl : LoginModel{
    
    override fun login(username: String, password: String, listener: LoginModel.OnListener) {
        DataUtil.clientLoginApi.login(username,password,object : CallBack<UserJson>{
            override fun callback(data: UserJson) {
                if(data.account == null){
                    //说明登录失败，账号密码错误...
                    listener.loginFailure()
                }else{
                    //登录成功
                    listener.loginSuccess(User(data.account?.id?:-1,data.profile?.nickname))
                }
            }

            override fun error(errorMsg: String) {
                listener.loginError(errorMsg)
            }
        })   
    }
    
}