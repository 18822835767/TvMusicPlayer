package com.example.tvmusicplayer.bean

class CircleButtonBean {
    var text : String? = null
    var imageResource : Int? = null
    
    constructor(){
        
    }

    constructor(text: String?, imageResource: Int?) {
        this.text = text
        this.imageResource = imageResource
    }
}