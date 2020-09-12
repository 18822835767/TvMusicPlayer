package com.example.repository.GsonBean

/**
 * 获取音乐url接口:baseUrl/song/url?id=191528,191527
 * Gson解析的bean类.
 * */
class MusicUrl {
    var data : MutableList<Data>? = null
    var code : Int? = null
    
    class Data{
        var id : Long? = null
        var url : String? = null
        var size : Long? = null
        var type : String? = null
    }
}