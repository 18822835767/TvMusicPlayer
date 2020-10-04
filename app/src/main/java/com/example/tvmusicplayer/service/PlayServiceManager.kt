package com.example.tvmusicplayer.service

import com.example.tvmusicplayer.IPlayInterface
import com.example.tvmusicplayer.IPlayObserver
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.NULL_INT_FLAG
import com.example.tvmusicplayer.util.LogUtil

/**
 * 管理音乐播放相关的方法.
 * */
object PlayServiceManager {
    private val TAG = "PlayServiceManager"
    private var service : IPlayInterface? = null
    
    fun init(service : IPlayInterface?){
        this.service = service
    }
    
    fun playSongs(songs : List<Song>,position : Int){
        LogUtil.d(TAG,"$position")
        service?.playSongs(songs, position)
    }
    
    fun playOrPause(){
        service?.playOrPause()
    }
    
    fun playNext(){
        service?.playNext()
    }
    
    fun playPre(){
        service?.playPre()
    }
    
    fun seekTo(seek : Int){
        service?.seekTo(seek)
    }
    
    fun setPlayMode(mode : Int){
        service?.playMode = mode
    }
    
    fun getQueueSongs() : MutableList<Song>{
        //todo 这里有点问题吧...
        return service?.queueSongs as MutableList<Song>
    }
    
    fun getCurrentPosition() : Int{
        return service?.currentPosition?:NULL_INT_FLAG
    }
    
    fun setCurrentPosition(currentPosition : Int){
        service?.currentPosition = currentPosition
    }
    
    fun getPlayMode() : Int{
        return service?.playMode?: NULL_INT_FLAG
    }
    
    fun getPlayState() : Int{
        return service?.playState?: NULL_INT_FLAG
    }
    
    fun registerObserver(observer : IPlayObserver){
        service?.registerObserver(observer)
    }
    
    fun unregisterObserver(observer : IPlayObserver){
        service?.unregisterObserver(observer)
    }
    
    fun getCurrentSong() : Song?{
        return service?.currentSong
    }
    
    fun getDuration() : Int{
        return service?.duration?:0
    }
    
    fun getCurrentPoint() : Int{
        return service?.currenPoint?:0
    }
    
    fun addNext(song : Song?){
        service?.addNext(song)
    }
}