package com.example.tvmusicplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.util.Constant
import java.util.*

class PlayService : Service() {

    private var mediaPlayer = MediaPlayer()
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
    
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }


    private inner class SeekTimeTask : TimerTask() {
        override fun run() {

        }
    }

}
