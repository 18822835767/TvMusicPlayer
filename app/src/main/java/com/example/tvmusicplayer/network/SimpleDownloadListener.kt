package com.example.tvmusicplayer.network

abstract class SimpleDownloadListener : DownloadListener{
    override fun onStart() {
    }

    override fun onProgress(progress : Int) {
    }

    override fun onSuccess() {
    }

    override fun onFailed() {
    }

    override fun onPaused() {
    }

    override fun onCancelled() {
    }
    
}