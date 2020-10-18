package com.example.tvmusicplayer.model.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.tvmusicplayer.util.Constant

object DBManager {
    private var helper : DatabaseHelper? = null
    var database : SQLiteDatabase? = null

    fun onInit(context : Context){
        helper = DatabaseHelper(context,Constant.DBConstant.DB_NAME,null,
            Constant.DBConstant.CURRENT_VERSION)
        helper?.let { 
            database = it.readableDatabase
        }
    }
    
    fun onClose(){
        helper?.close()
    }
}