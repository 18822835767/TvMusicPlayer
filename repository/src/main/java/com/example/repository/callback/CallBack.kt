package com.example.repository.callback

interface CallBack<T>{
    fun callback(data : T)
    
    fun error(errorMsg : String)
}