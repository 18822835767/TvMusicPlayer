package com.example.tvmusicplayer.model.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tvmusicplayer.util.LogUtil

class DatabaseHelper(
    context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        private const val CREATE_DOWNLOAD_SONG: String = ("create table DownloadSong("
                + "download_id integer primary key autoincrement,"
                + "song_id integer unique,"
                + "url text,"
                + "name text,"
                + "br integer,"
                + "artist_name text,"
                + "pic_url text,"
                + "first_letter text,"
                + "online integer)")
        
        private const val TAG = "DatabaseHelper"
    }

    override fun onCreate(db: SQLiteDatabase?) {
            db?.let { 
                it.execSQL(CREATE_DOWNLOAD_SONG)
                LogUtil.d(TAG,"onCreate：")
            }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.let { 
                it.execSQL("drop table if exists DownloadSong")
                onCreate(it)
            }
    }

}