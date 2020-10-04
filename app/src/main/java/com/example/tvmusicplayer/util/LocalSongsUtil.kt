package com.example.tvmusicplayer.util

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.example.tvmusicplayer.MyApplication
import com.example.tvmusicplayer.bean.Song

/**
 * 音乐扫描类.
 * */
class LocalSongsUtil {
    companion object {
        fun getLocalSong(): MutableList<Song> {
            val songs = mutableListOf<Song>()
            val cursor: Cursor? = MyApplication.app?.contentResolver?.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER
            )
//            val cursor: Cursor? = context.contentResolver.query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//                , null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER
//            )
            cursor?.let { c ->
                while (c.moveToNext()) {
                    val id: Long = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    //获取的url例如：/storage/emulated/0/Music/陈奕迅 - 爱情转移 [mqms2] (1).mp3
                    val url: String =
                        c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val name: String =
                        c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val artistName: String =
                        c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val size: Long = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                    val albumId: Long =
                        c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    //todo 图片的url
                    val isMusic: Int =
                        c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC))
                    if (isMusic != 0) {
                        val s = Song()
                        s.id = id
                        s.url = url
                        s.size = size
                        s.name = name
                        s.artistName = artistName
                        s.picUrl = "content://media/external/audio/media/$albumId/albumart"
                        s.firstLetter = PinyinUtil.getHeaderLetter(name)
                        songs.add(s)
                    }
                }
                c.close()
            }
            return songs
        }

        private fun getAlbumPic(albumId: Long): String? {
            val uriAlbums = "content://media/external/audio/albums"
            val projections = arrayOf<String>("album_art")
            var albumPic: String? = null
            val cur: Cursor? = MyApplication.app?.contentResolver?.query(
                Uri.parse("$uriAlbums/$albumId"), projections, null,
                null, null
            )
            cur?.let { 
                if(it.count > 0 && it.columnCount > 0){
                    it.moveToNext()
                    albumPic = it.getString(0)
                }
                it.close()
            }
            return albumPic
        }
    }
}