package com.example.tvmusicplayer.manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.NotificationTarget
import com.example.tvmusicplayer.IPlayObserver
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.detail.DetailActivity
import com.example.tvmusicplayer.service.SimplePlayObserver
import com.example.tvmusicplayer.util.Constant
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PAUSE
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PLAY
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
    private var ctrlNotification: Notification? = null
    private val receiver = SongCtrlReceiver()
    var remoteCommunicator: RemoteCommunicator? = null

    /**
     * 利用这个去进行RemoteView的图片的更新.
     * */
    private var target: NotificationTarget? = null

    private val observer = object : SimplePlayObserver() {
        /**
         * 好奇怪，这里是运行在main线程里的？
         * */
        override fun onPlayStateChange(playState: Int) {
            when (playState) {
                PLAY_STATE_PLAY -> {
                    remoteCtrlView?.setImageViewResource(
                        R.id.remote_action,
                        R.drawable.ic_remote_pause
                    )
                    updateCtrlView()
                }
                PLAY_STATE_PAUSE -> {
                    remoteCtrlView?.setImageViewResource(
                        R.id.remote_action,
                        R.drawable.ic_remote_play
                    )
                    updateCtrlView()
                }
            }
        }

        override fun onSongChange(song: Song?, position: Int) {
            song?.let { s ->
                remoteCtrlView?.let {
                    setUIInfo(it,s)
                    updateCtrlView()
                }
            }
        }

        override fun onSongsEmpty() {
            remoteCtrlView?.let {
                it.setTextViewText(R.id.song_name, "歌曲名字")
                it.setTextViewText(R.id.singer_name, "歌手")
                it.setImageViewResource(R.id.remote_action, R.drawable.ic_remote_play)
                updateCtrlView()
            }
        }
    }

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
                ctrlNotification = builder.setSmallIcon(R.drawable.ic_notify)
                    .setContentTitle("音乐播放器")
                    .setContentText("控制一下")
                    .setOngoing(true)
                    .setCustomBigContentView(remoteCtrlView)
                    .build()
                setCtrlClickEvent()
                initUIInfo()
                LogUtil.d(TAG, "展示音乐播放的RemoteView")
                manager?.notify(Constant.RemoteSongCtrlConstant.CTRL_ID, ctrlNotification)
                target = NotificationTarget(context, R.id.play_cover, remoteCtrlView,
                    ctrlNotification,Constant.RemoteSongCtrlConstant.CTRL_ID)
                //注册音乐播放的观察者
                remoteCommunicator?.registerObserver(observer)
            }
        }
    }

    /**
     * 为RemoteView增加点击事件的监听.
     * */
    private fun setCtrlClickEvent() {
        //启动歌曲详情页的活动
        ctrlNotification?.contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, DetailActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )

        //发送一系列的广播.
        remoteCtrlView?.let {

            it.setOnClickPendingIntent(
                R.id.remote_cancel, PendingIntent.getBroadcast(
                    context, 0, Intent(Constant.RemoteSongCtrlConstant.CTRL_CANCEL),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            it.setOnClickPendingIntent(
                R.id.remote_pre, PendingIntent.getBroadcast(
                    context, 0, Intent(Constant.RemoteSongCtrlConstant.CTRL_PRE),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            it.setOnClickPendingIntent(
                R.id.remote_action, PendingIntent.getBroadcast(
                    context, 0, Intent(Constant.RemoteSongCtrlConstant.CTRL_ACTION),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            it.setOnClickPendingIntent(
                R.id.remote_next, PendingIntent.getBroadcast(
                    context, 0, Intent(Constant.RemoteSongCtrlConstant.CTRL_NEXT),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
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
            ctrlNotification = null
            target = null
            //取消音乐播放观察者的注册
            remoteCommunicator?.unRegisterObserver(observer)
        }
    }

    /**
     * 当RemoteView弹出时，初始化一些信息.
     * */
    private fun initUIInfo() {
        remoteCommunicator?.getCurrentSong()?.let { s ->
            remoteCtrlView?.let {
                setUIInfo(it,s)
            }
        }
    }
    
    private fun setUIInfo(remoteViews: RemoteViews,song : Song){
        remoteViews.setTextViewText(R.id.song_name, song.name ?: "")
        remoteViews.setTextViewText(R.id.singer_name, song.artistName ?: "")
        context?.let {c->
            //如果是在线歌曲
            if(song.online){
                val options : RequestOptions = RequestOptions()
                    .placeholder(R.drawable.empty_grey)
                    .error(R.drawable.empty_grey)

                target?.let {t->
                    Glide.with(c)
                        .asBitmap()
                        .load(song.picUrl)
                        .apply(options)
                        .into(t)
                }

                //如果是本地歌曲
            }else{
                target?.let {t->
                    Glide.with(c)
                        .asBitmap()
                        .load(R.drawable.empty_grey)
                        .into(t)
                }
            }
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
    fun unRegisterRemoteReceiver() {
        LogUtil.d(TAG, "取消注册广播接收器")
        context?.unregisterReceiver(receiver)
    }

    /**
     * 更新RemoteView.
     * */
    private fun updateCtrlView() {
        manager?.notify(Constant.RemoteSongCtrlConstant.CTRL_ID, ctrlNotification)
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
            intent?.let { i ->
                when (i.action) {
                    Constant.RemoteSongCtrlConstant.CTRL_ACTION -> {
                        remoteCommunicator?.action()
                    }

                    Constant.RemoteSongCtrlConstant.CTRL_CANCEL -> {
                        closeCtrlView()
                        remoteCommunicator?.cancel()
                    }

                    Constant.RemoteSongCtrlConstant.CTRL_NEXT -> {
                        remoteCommunicator?.next()
                    }

                    Constant.RemoteSongCtrlConstant.CTRL_PRE -> {
                        remoteCommunicator?.pre()
                    }
                    else -> {

                    }
                }
            }
        }
    }

    interface RemoteCommunicator {
        fun action()
        fun cancel()
        fun next()
        fun pre()

        fun registerObserver(observer: IPlayObserver)
        fun unRegisterObserver(observer: IPlayObserver)

        fun getCurrentSong(): Song?
    }
}