package com.example.tvmusicplayer.bean

/**
 * 歌单的实体类.
 * */
class PlayList{
    var id : Long? = null
    var name : String? = null
    var coverImgUrl : String? = null
    
    constructor(){}
    
    constructor(id : Long?, name : String?, coverImgUrl : String?){
        this.id = id
        this.name = name
        this.coverImgUrl = coverImgUrl
    }
}