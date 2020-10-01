package com.example.tvmusicplayer.model.impl

import com.example.data.DataUtil
import com.example.repository.RequestCallBack
import com.example.repository.bean.LyricJson
import com.example.repository.bean.SongPlayJson
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.model.SongInfoModel
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.NULL_LONG_FLAG
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.NULL_URL

class SongInfoModelImpl : SongInfoModel{
    override fun getSongPlayInfo(song: Song,listener : SongInfoModel.OnSongPlayInfoListener) {
       song.id?.let {
           DataUtil.clientMusicApi.getSongPlay(it,object : RequestCallBack<SongPlayJson>{
               override fun callback(data: SongPlayJson) {
                   data.data?.let {
                       val info : SongPlayJson.Data = it[0]
                       song.url = info.url?:NULL_URL
                       song.size = info.size?:NULL_LONG_FLAG
                       song.br = info.br?: NULL_LONG_FLAG
                   }
                   listener.getSongPlayInfoSuccess(song)
               }

               override fun error(errorMsg: String) {
                   listener.error(errorMsg)
               }
           })   
       }

    }

    override fun getSongLyrics(id: Long,listener : SongInfoModel.OnLyricsListener) {
        DataUtil.clientMusicApi.getSongLyric(id,object : RequestCallBack<LyricJson>{
            override fun callback(data: LyricJson) {
                data.lrc?.lyric?.let { 
                    listener.getLyricsSuccess(it)
                }
            }

            override fun error(errorMsg: String) {
                listener.error(errorMsg)
            }

        })
    }

}