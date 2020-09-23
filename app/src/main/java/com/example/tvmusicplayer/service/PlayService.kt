package com.example.tvmusicplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.RemoteCallbackList
import com.example.tvmusicplayer.IPlayInterface
import com.example.tvmusicplayer.IPlayObserver
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.util.Constant
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.NULL_URL
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.ORDER_PLAY
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PAUSE
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PLAY
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.RANDOM_PLAY
import com.example.tvmusicplayer.util.LogUtil
import java.util.*

class PlayService : Service() {

    private val TAG ="PlayService"
    private var mediaPlayer: MediaPlayer? = null
    private var timer: Timer? = null
    private var songs = mutableListOf<Song>()
    private var observers = RemoteCallbackList<IPlayObserver>()

    /**
     * 当前的播放状态.
     * */
    private var currentState = Constant.PlaySongConstant.PLAY_STATE_STOP

    /**
     * 定时任务.
     * */
    private var seekTimerTask: SeekTimeTask? = null

    /**
     * 标志是否是第一次播放.
     * */
    private var firstPlay = true

    /**
     * 记录是否调用了MediaPlayer的reset()方法
     * */
    private var hasReset = false

    /**
     * 记录当前播放的歌曲在list中的位置.
     * */
    private var currentPosition = 0

    /**
     * 当前的播放模式.
     * */
    private var playMode = ORDER_PLAY

    /**
     * 标记是否开启了定时任务.
     * */
    private var startTimer = false

    override fun onCreate() {
        super.onCreate()
        initMediaPlayer()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private fun initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        mediaPlayer?.let {
            // todo 增加监听
            it.setOnPreparedListener { mp ->
                if (mp != null) {
                    mp.start()
                    currentState = PLAY_STATE_PLAY
                    onPlayStateChange()
                    if (!startTimer) {
                        startTimer()
                    }
                }
            }

            it.setOnCompletionListener { _ ->
                playNextSong()
            }
            
            it.setOnErrorListener { mp, what, extra -> 
                LogUtil.d(TAG,"onError")   
                true
            }

            //todo 测试用
            it.setDataSource("http://m7.music.126.net/20200915234252/d5cc67998d2177181bec7baa" +
                    "997c6a78/ymusic/9aaf/4d9f/d448/379b8b6085228dd5258fda3035313546.mp3")
            it.prepareAsync()
        }
    }

    override fun onDestroy() {
        mediaPlayer?.let {
            it.stop()
            it.release()
            mediaPlayer = null
        }
        super.onDestroy()
    }

    private fun onPlayStateChange() {
        //遍历观察者，通知播放状态的改变.
        val size = observers.beginBroadcast()
        for (i in 0 until size) {
            val observer = observers.getBroadcastItem(i)
            observer.onPlayStateChange(currentState)
        }
        observers.finishBroadcast()
    }

    private fun onSeekChange() {
        //遍历观察者
        val size = observers.beginBroadcast()
        for (i in 0 until size) {
            val observer = observers.getBroadcastItem(i)
            observer.onSeekChange(currentPosition)
        }
        observers.finishBroadcast()
    }

    private var binder = object : IPlayInterface.Stub() {
        override fun playSongs(songs: MutableList<Song>?, position: Int) {
            this@PlayService.playSongs(songs, position)
        }

        override fun registerObserver(observer: IPlayObserver?) {
            observers.register(observer)
        }

        override fun seekTo(seek: Int) {
            this@PlayService.seekTo(seek)
        }

        override fun getCurrentPosition(): Int {
            return this@PlayService.currentPosition
        }

        override fun getPlayState(): Int {
            return this@PlayService.currentState
        }

        override fun unregisterObserver(observer: IPlayObserver?) {
            observers.unregister(observer)
        }

        override fun playOrPause() {
            this@PlayService.playOrPause()
        }

        override fun playPre() {
            playPreSong()
        }

        override fun getQueueSongs(): MutableList<Song> {
            return songs
        }

        override fun setCurrentPosition(currentPosition: Int) {
            this@PlayService.currentPosition = currentPosition
        }

        override fun getPlayMode(): Int {
            return this@PlayService.playMode
        }

        override fun playNext() {
            playNextSong()
        }

        override fun setPlayMode(mode: Int) {
            this@PlayService.playMode = mode
        }
    }

    private fun playOrPause() {
        if (songs.size == 0) {
            //todo 给点没有歌的提示
            return;
        }
        when (currentState) {
            PLAY_STATE_PLAY -> {
                mediaPlayer?.let {
                    it.pause()
                    currentState = PLAY_STATE_PAUSE
                    stopTimer()
                }
            }
            PLAY_STATE_PAUSE -> {
                mediaPlayer?.let {
                    it.start()
                    currentState = PLAY_STATE_PLAY
                    startTimer()
                }
            }
        }
        //遍历观察者，通知播放状态的改变.
        onPlayStateChange()
    }

    private fun performSong(dataSource: String) {
        if (mediaPlayer == null) {
            initMediaPlayer()
        }

        //若音乐正在播放并且定时任务开着，那么关闭定时任务
        if (currentState == PLAY_STATE_PLAY && startTimer) {
            stopTimer()
        }

        mediaPlayer?.let {
            hasReset = true
            it.reset()
            it.setDataSource(dataSource)
            it.prepareAsync()
            //todo 这里少了个pause()
        }

    }

    private fun playNextSong() {
        if (songs.size == 0) {
            //todo 给点没歌的提示
            return
        }

        when (playMode) {
            //列表循环播放
            ORDER_PLAY -> currentPosition = (currentPosition + 1) % songs.size
            //随机播放
            RANDOM_PLAY -> currentPosition = (Math.random() * songs.size).toInt()
            ////单曲循环不用做处理
            else -> {
            }
        }
        performSong(songs[currentPosition].url ?: NULL_URL)
    }

    private fun playPreSong() {
        if (songs.size == 0) {
            //todo 给点没歌的提示
            return
        }

        when (playMode) {
            //列表循环播放
            ORDER_PLAY -> if (currentPosition == 0) {
                currentPosition = songs.size - 1
            } else {
                currentPosition--
            }
            //随机播放
            RANDOM_PLAY -> currentPosition = (Math.random() * songs.size).toInt()
            //单曲循环不用做处理
            else -> {
            }
        }
        performSong(songs[currentPosition].url ?: NULL_URL)
    }

    private fun playSongs(songList: MutableList<Song>?, position: Int) {
        songList?.let {
            songs.clear()
            songs.addAll(it)
            currentPosition = position

            performSong(songs[position].url ?: NULL_URL)
        }
    }

    private fun seekTo(seek: Int) {
        mediaPlayer?.let {
            val currentProcess = (seek * 1.0f / 100 * it.duration).toInt()
            it.seekTo(currentProcess)
        }
    }

    private fun startTimer() {
        if (timer == null) {
            timer = Timer()
        }
        if (seekTimerTask == null) {
            seekTimerTask = SeekTimeTask()
        }
        timer?.schedule(seekTimerTask, 0, 300)
        startTimer = true
    }

    private fun stopTimer() {
        timer?.let {
            it.cancel()
            timer = null
        }

        seekTimerTask?.let {
            it.cancel()
            seekTimerTask = null
        }

        startTimer = false
    }

    private inner class SeekTimeTask : TimerTask() {
        override fun run() {
            mediaPlayer?.let {
                //当前播放到的时间
                val currentTimePoint = it.currentPosition
                //播放进度的百分比，用于控制进度条
                currentPosition = (currentTimePoint * 1.0f * 100 / it.duration).toInt()
                //遍历观察者
                onSeekChange()
            }
        }
    }

}
