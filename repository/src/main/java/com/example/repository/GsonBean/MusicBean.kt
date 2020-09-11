package com.example.repository.GsonBean

/**
 * 接口:baseUrl/song/url?id=191528,191527
 * */
class MusicBean {
    var data : MutableList<Data>? = null
    var code : Int? = null
    
    class Data{
        var id : Long? = null
        var url : String? = null
        var size : Long? = null
        var type : String? = null
    }
}