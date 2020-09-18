package com.example.data

interface RequestCallBack<T>{
    fun callback(data : T)
    
    fun error(errorMsg : String)
}