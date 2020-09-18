package com.example.repository.callback

interface RequestCallBack<T>{
    fun callback(data : T)
    
    fun error(errorMsg : String)
}