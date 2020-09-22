package com.example.tvmusicplayer

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.tvmusicplayer.model.LoginModel
import com.example.tvmusicplayer.model.impl.LoginModelImpl
import com.example.tvmusicplayer.service.PlayService
import com.example.tvmusicplayer.util.LogUtil

class MyApplication : Application() {
    private val loginModel: LoginModel = LoginModelImpl()
    private val TAG = "MyApplication"
    private var service : IPlayInterface? = null
    
    override fun onCreate() {
        super.onCreate()

        //获取登陆的信息.
        loginModel.getLoginStatus()
        
        val intent = Intent(this,PlayService::class.java)
        startService(intent)
        bindService(intent,connection, Context.BIND_AUTO_CREATE)
    }
    
    private val connection : ServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            LogUtil.d(TAG,"onServiceConnected: ")
            //获取IPlayInterface接口，给客户端调用
            this@MyApplication.service = IPlayInterface.Stub.asInterface(service)
        }
        
        override fun onServiceDisconnected(name: ComponentName?) {
            LogUtil.d(TAG,"onServiceDisconnected: ")
            //todo 处理
        }
        
    }
}