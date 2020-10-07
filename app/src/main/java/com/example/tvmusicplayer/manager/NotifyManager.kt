package com.example.tvmusicplayer.manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import com.example.tvmusicplayer.R

/**
 * 通知的管理者.
 * */
object NotifyManager {
    private var manager: NotificationManager? = null
    private var channel: NotificationChannel? = null
    private var context: Context? = null
    private val channelName = "channel_name"
    private val channelId = "channel_id"

    fun init(context: Context) {
        this.context = context
        manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager?.createNotificationChannel(channel!!)
        }
    }

    fun downloadProgress(songId: Long?, name: String?, progress: Int) {
        if (songId != null && name != null) {
            val builder: Notification.Builder?
            //若安卓版本大于等于8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = Notification.Builder(context, channelId)
                //若版本小于8.0
            } else {
                builder = Notification.Builder(context)
                builder.setPriority(Notification.PRIORITY_DEFAULT)
            }
            
            builder.setSmallIcon(R.mipmap.ic_launcher)
            builder.setContentTitle(name)
            if(progress >= 0){
                builder.setContentText("${progress}%")
                builder.setProgress(100,progress,false)
            }
            manager?.notify(songId.toInt(),builder.build())
        }
    }

}