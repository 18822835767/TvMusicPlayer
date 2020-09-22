package com.example.tvmusicplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.util.Constant
import com.example.tvmusicplayer.util.Constant.PlayMusicConstant.PLAY_STATE_PAUSE
import com.example.tvmusicplayer.util.Constant.PlayMusicConstant.PLAY_STATE_PLAY
import java.util.*

class PlayService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var timer: Timer? = null
    private var songs = mutableListOf<Song>()

    /**
     * 当前的播放状态.
     * */
    private var currentState = Constant.PlayMusicConstant.PLAY_STATE_STOP

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
    private var playMode = Constant.PlayMusicConstant.ORDER_PLAY

    /**
     * 标记是否开启了定时任务.
     * */
    private var startTimer = false

    override fun onCreate() {
        super.onCreate()
        initMediaPlayer()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private fun initMediaPlayer() {
        mediaPlayer?.let {
            this.mediaPlayer = MediaPlayer()
            // todo 增加监听
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
        //todo 遍历观察者
    }

    private fun startTimer() {

    }

    private fun stopTimer() {

    }

    private inner class SeekTimeTask : TimerTask() {
        override fun run() {

        }
    }

}
