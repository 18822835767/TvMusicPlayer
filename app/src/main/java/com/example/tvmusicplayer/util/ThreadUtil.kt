package com.example.tvmusicplayer.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 线程调度工具.
 * */
class ThreadUtil {
    companion object {
        private val mainHandler: Handler = Handler(Looper.getMainLooper())
        private val threadPool : ExecutorService = Executors.newCachedThreadPool()
        
        fun runOnUi(runnable: Runnable) {
            mainHandler.post(runnable)
        }
        
        fun runOnThreadPool(runnable: Runnable){
            threadPool.execute(runnable)
        }
    }
}