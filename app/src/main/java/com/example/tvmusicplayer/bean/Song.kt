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

    constructor() {
    }

    constructor(
        id: Long?,
        url: String?,
        size: Long?,
        name: String?,
        br: Long?,
        artistName: String?,
        picUrl: String?
    ) {
        this.id = id
        this.url = url
        this.size = size
        this.name = name
        this.br = br
        this.artistName = artistName
        this.picUrl = picUrl
    }

    override fun toString(): String {
        return "id:$id,url:$url,size:$size,name:$name,br:$br,artistName:$artistName,picUrl:$picUrl"
    }
}