package com.example.tvmusicplayer.model.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.model.DownloadSongModel
import com.example.tvmusicplayer.model.dao.DBManager
import com.example.tvmusicplayer.util.Constant

class DownloadSongModelImpl : DownloadSongModel {
    override fun insert(song: Song) {
        DBManager.database?.let { db ->
            val values = ContentValues()
            values.put("song_id", song.id)
            values.put("url", song.url)
            values.put("name", song.name)
            values.put("br", song.br)
            values.put("artist_name", song.artistName)
            values.put("pic_url", song.picUrl)
            values.put("first_letter", song.firstLetter.toInt())
            values.put("online", 0)
            //插入有冲突，则替换掉原来的数据.
            db.insertWithOnConflict(
                Constant.DBConstant.DOWNLOAD_TABLE, null, values,
                SQLiteDatabase.CONFLICT_REPLACE
            )
        }
    }

    override fun delete(songId: Long) {
        DBManager.database?.delete(
            Constant.DBConstant.DOWNLOAD_TABLE, "song_id = ", arrayOf(songId.toString())
        )
    }

    override fun querySongPath(songId: Long, listener: DownloadSongModel.OnListener) {
        DBManager.database?.let {db-> 
            var path : String? = null
            val cursor = db.query(Constant.DBConstant.DOWNLOAD_TABLE, arrayOf("url"),
                "song_id = ", arrayOf(songId.toString()),null,null,
                null)
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex("url"))
            }
            cursor.close()
            listener.querySongPathSuccess(path)
        }
    }

}