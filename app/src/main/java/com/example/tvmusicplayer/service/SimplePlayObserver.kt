package com.example.tvmusicplayer.service

import com.example.tvmusicplayer.IPlayObserver
import com.example.tvmusicplayer.bean.Song

/**
 * 观察者的模板，使得子类不用实现所有的方法.
 * */
abstract class SimplePlayObserver : IPlayObserver.Stub() {
    override fun onPlayStateChange(playState: Int) {

    }

    override fun onSeekChange(currentPosition: Int) {

    }

    override fun onSongChange(song: Song?) {

    }

    /**
     * 当播放的歌曲的list没歌曲了，回调该方法.
     * */
    override fun onSongsEmpty() {
        
    }
}