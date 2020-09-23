package com.example.tvmusicplayer.model.impl

import com.example.data.DataUtil
import com.example.repository.RequestCallBack
import com.example.repository.bean.SongPlayJson
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.model.SongInfoModel
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.NULL_LONG_FLAG
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.NULL_URL

class SongInfoModelImpl : SongInfoModel{
    override fun getSongPlayInfo(song: Song, id: Long,listener : SongInfoModel.OnListener) {
        DataUtil.clientMusicApi.getSongPlay(id,object : RequestCallBack<SongPlayJson>{
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