package com.example.tvmusicplayer.base

interface BaseModelListener {
    /**
     * 目前感觉这个方法设计的不太好。因为这样设计一个通用的error()方法，不利于Model层对不同的错误
     * 进行回调，只能回调这一个方法。比如 搜索歌曲错误 和 加载更多错误，回调路径为：model -> presenter
     * -> view，而两种错误可能View层的处理方式不同，所以这里最好不要限定一个error()方法在这.
     * */
    fun error(msg : String)
}