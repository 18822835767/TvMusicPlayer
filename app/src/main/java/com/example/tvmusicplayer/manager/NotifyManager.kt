package com.example.tvmusicplayer.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build

/**
 * 通知的管理者.
 * */
object NotifyManager {
    private var manager : NotificationManager? = null
    private var channel : NotificationChannel? = null
    private var context : Context? = null
    private val channelName = "channel_name"
    private val channelId = "channel_id"
    
    fun init(context: Context){
        this.context = context
        manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            channel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_DEFAULT)
            manager?.createNotificationChannel(channel!!)
        }
    }
    
    fun downloadProgress(songId : Long?,name : String?,progress : Int){
        
    }
    
}