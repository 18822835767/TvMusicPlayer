package com.example.tvmusicplayer.network

interface DownloadListener {
    fun onStart()
    
    fun onProgress(progress : Int)
    
    fun onSuccess()
    
    fun onFailed()
    
    fun onPaused()
    
    fun onCancelled()
}