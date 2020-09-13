package com.example.tvmusicplayer.bean

/**
 * 歌词的实体类.
 * */
class Lyrics {
    var text : String? = null
    var start : Long? = null
    
    constructor(){
    }
    
    constructor(text : String,start : Long){
        this.text = text
        this.start = start
    }
}