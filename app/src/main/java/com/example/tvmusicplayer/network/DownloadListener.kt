package com.example.tvmusicplayer.network

interface DownloadListener {
    fun onStart()
    
    fun onProgress()
    
    fun onSuccess()
    
    fun onFailed()
    
    fun onPaused()
    
    fun onCancelled()
}