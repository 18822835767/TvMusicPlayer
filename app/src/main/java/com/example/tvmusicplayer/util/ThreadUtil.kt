package com.example.tvmusicplayer.util

import android.os.Handler
import android.os.Looper

/**
 * 线程调度工具.
 * */
class ThreadUtil {
    companion object {
        private val mainHandler: Handler = Handler(Looper.getMainLooper())

        fun runOnUi(runnable: Runnable) {
            mainHandler.post(runnable)
        }
    }
}