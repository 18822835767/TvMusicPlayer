package com.example.tvmusicplayer

import android.app.ActivityManager
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Process
import com.example.tvmusicplayer.model.LoginModel
import com.example.tvmusicplayer.model.impl.LoginModelImpl
import com.example.tvmusicplayer.service.PlayService
import com.example.tvmusicplayer.service.PlayServiceManager
import com.example.tvmusicplayer.util.LogUtil


class MyApplication : Application() {
    private val TAG = "MyApplication"
    private var service : IPlayInterface? = null
    private val MAIN_PROCESS_NAME = "com.example.tvmusicplayer"
    
    override fun onCreate() {
        super.onCreate()
        
        if(isAppMainProcess()){
            val loginModel : LoginModel = LoginModelImpl()
            loginModel.getLoginStatus()
            
            val intent = Intent(this,PlayService::class.java)
            startService(intent)
            bindService(intent,connection, Context.BIND_AUTO_CREATE)
        }
        
        val pid = Process.myPid()
        val process : String = getAppNameByPID(this,pid)?:"Null_process"
        LogUtil.d(TAG,"MyApplication onCreate,processName : $process")
    }
    
    private val connection : ServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            LogUtil.d(TAG,"onServiceConnected: ")
            //获取IPlayInterface接口，给客户端调用
            this@MyApplication.service = IPlayInterface.Stub.asInterface(service)
            PlayServiceManager.init(this@MyApplication.service)
        }
        
        override fun onServiceDisconnected(name: ComponentName?) {
            LogUtil.d(TAG,"onServiceDisconnected: ")
            //todo 处理
        }
        
    }
    
    /**
     *
     * 判断是不是UI主进程，因为有些东西只能在UI主进程初始化
     *
     */
    private fun isAppMainProcess(): Boolean {
        return try {
            val pid = Process.myPid()
            val process: String = getAppNameByPID(this, pid)?:"Null_process"
            process == MAIN_PROCESS_NAME
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }

    /**
     *
     * 根据Pid得到进程名
     *
     */
    private fun getAppNameByPID(context: Context, pid: Int): String? {
        val manager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return ""
    }
}