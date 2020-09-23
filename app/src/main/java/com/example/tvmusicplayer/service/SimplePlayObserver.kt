package com.example.tvmusicplayer.service

import com.example.tvmusicplayer.IPlayObserver

/**
 * 观察者的模板，使得子类不用实现所有的方法.
 * */
abstract class SimplePlayObserver : IPlayObserver.Stub() {
    override fun onPlayStateChange(playState: Int) {

    }

    override fun onSeekChange(currentPosition: Int) {

    }
}