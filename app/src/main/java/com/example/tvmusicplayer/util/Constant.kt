package com.example.tvmusicplayer.util

class Constant{
    object UserFragmentConstant{
        const val LOCAL_MUSIC = "本地音乐"
        const val DOWNLOAD_MANAGER = "下载管理"
        const val MY_SONG_LIST = "我的歌单"
    }

    /**
     * 这里的object 类名 相当于java 的 public static final class
     * 播放音乐对应的常量.
     */
    object PlaySongConstant {
        //不同的播放模式
        const val ORDER_PLAY = 0 //列表循环播放
        const val RANDOM_PLAY = 1 //随机播放
        const val LOOP_PLAY = 2 //单曲循环

        //表示播放状态
        const val PLAY_STATE_PLAY = 10 //播放
        const val PLAY_STATE_PAUSE = 11 //暂停
        const val PLAY_STATE_STOP = 12 //停止
        
        //Null的url
        const val NULL_URL = ""
        
        const val NULL_INT_FLAG = -1
    }
}