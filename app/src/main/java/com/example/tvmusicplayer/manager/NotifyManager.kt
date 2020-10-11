package com.example.tvmusicplayer.manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.util.Constant
import com.example.tvmusicplayer.util.LogUtil

/**
 * 通知的管理者.
 * */
object NotifyManager {

    private val TAG = "NotifyManager"

    private var manager: NotificationManager? = null
    private var channel: NotificationChannel? = null
    private var context: Context? = null
    private val channelName = "channel_name"
    private val channelId = "channel_id"
    private var remoteCtrlView: RemoteViews? = null
    private val receiver = SongCtrlReceiver()

    fun init(context: Context) {
        this.context = context
        manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager?.createNotificationChannel(channel!!)
        }
    }

    /**
     * 展示通知栏的RemoteView.
     * */
    fun showCtrlView() {
        if (remoteCtrlView == null) {
            context?.let { context ->
                remoteCtrlView = RemoteViews(context.packageName, R.layout.play_ctrl_notification)
                val builder = NotificationCompat.Builder(context, channelId)
                builder.setSmallIcon(R.drawable.ic_notify)
                    .setContentTitle("音乐播放器")
                    .setContentText("控制一下")
                    .setOngoing(true)
                    .setCustomBigContentView(remoteCtrlView)
                LogUtil.d(TAG, "展示音乐播放的RemoteView")
                manager?.notify(Constant.RemoteSongCtrlConstant.CTRL_ID, builder.build())
            }
        }
    }

    /**
     * 关掉通知栏的RemoteView.
     * */
    fun closeCtrlView() {
        if (remoteCtrlView != null) {
            LogUtil.d(TAG, "关闭音乐播放的RemoteView")
            manager?.cancel(Constant.RemoteSongCtrlConstant.CTRL_ID)
            remoteCtrlView = null
        }
    }

    /**
     * 注册广播接收器.
     * */
    fun registerRemoteReceiver() {
        val filter = IntentFilter()
        filter.addAction(Constant.RemoteSongCtrlConstant.CTRL_ACTION)
        filter.addAction(Constant.RemoteSongCtrlConstant.CTRL_CANCEL)
        filter.addAction(Constant.RemoteSongCtrlConstant.CTRL_NEXT)
        filter.addAction(Constant.RemoteSongCtrlConstant.CTRL_PRE)
        LogUtil.d(TAG, "注册广播接收器")
        context?.registerReceiver(receiver, filter)
    }

    /**
     * 取消注册广播接收器.
     * */
    fun unRegisterReceiver() {
        LogUtil.d(TAG, "取消注册广播接收器")
        context?.unregisterReceiver(receiver)
    }

    fun downloadProgress(songId: Long?, title: String?, progress: Int) {
        if (songId != null && title != null) {
            val builder: Notification.Builder?
            //若安卓版本大于等于8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = Notification.Builder(context, channelId)
                //若版本小于8.0
            } else {
                builder = Notification.Builder(context)
                builder.setPriority(Notification.PRIORITY_DEFAULT)
            }

            builder.setSmallIcon(R.drawable.ic_notify)
            builder.setContentTitle(title)
            if (progress >= 0) {
                builder.setContentText("${progress}%")
                builder.setProgress(100, progress, false)
            }
            manager?.notify(songId.toInt(), builder.build())
        }
    }

    fun closeNotify(songId: Long?) {
        songId?.let {
            manager?.cancel(it.toInt())
        }
    }

    private class SongCtrlReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

        }
    }
}