package com.example.tvmusicplayer.util

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.example.tvmusicplayer.bean.Song

/**
 * 音乐扫描类.
 * */
class LocalSongsUtil {
    companion object {
        fun getLocalSong(context: Context): List<Song> {
            val songs = mutableListOf<Song>()
            val cursor: Cursor? = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER
            )
            cursor?.let {
                while (it.moveToNext()) {
                    val id: Long = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val url: String =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val name = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val artistName: String =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    //todo 图片的url
                    val isMusic: Int =
                        it.getInt(it.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC))
                    if (isMusic != 0) {
                        val s = Song()
                        s.id = id
                        s.url = url
                        s.name = name
                        s.artistName = artistName
                        songs.add(s)
                    }
                }
                it.close()
            }
            return songs
        }
    }
}