package com.example.tvmusicplayer.bean

class Song {
    var id: Long? = null
    var url: String? = null
    var size: Long? = null

    /**
     * 歌曲名字.
     * */
    var name: String? = null

    /**
     * 码率.
     * */
    var br: Long? = null
    
    /**
     * 歌手名字.
     * */
    var artistName: String? = null
    
    /**
     * 专辑图片的url.
     * */
    var picUrl: String? = null
}