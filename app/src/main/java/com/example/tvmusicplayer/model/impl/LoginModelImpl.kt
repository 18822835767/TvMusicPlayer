package com.example.tvmusicplayer.model.impl

import com.example.repository.DataUtil
import com.example.repository.GsonBean.UserJson
import com.example.tvmusicplayer.bean.User
import com.example.tvmusicplayer.model.LoginModel

class LoginModelImpl : LoginModel{
    
    override fun login(username: String, password: String, listener: LoginModel.OnListener) {
        DataUtil.clientLoginApi.login(username,password,object : RequestCallBack<UserJson>{
            override fun callback(data: UserJson) {
                if(data.profile == null){
                    //说明登录失败，账号密码错误...
                    listener.loginFailure()
                }else{
                    //登录成功
                    listener.loginSuccess(User(data.profile?.userId?:-1,data.profile?.nickname))
                }
            }

            override fun error(errorMsg: String) {
                listener.error(errorMsg)
            }
        })   
    }
    
}