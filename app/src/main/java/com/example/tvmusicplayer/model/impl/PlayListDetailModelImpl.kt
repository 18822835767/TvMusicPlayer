package com.example.tvmusicplayer.model.impl

import com.example.data.DataUtil
import com.example.repository.RequestCallBack
import com.example.repository.bean.SongDetailJson
import com.example.repository.bean.SongIdsJson
import com.example.repository.bean.SongPlayJson
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.model.PlayListDetailModel
import java.lang.StringBuilder

class PlayListDetailModelImpl : PlayListDetailModel {
    override fun getPlayListDetail(id: Long, listener: PlayListDetailModel.OnListener) {
        DataUtil.clientMusicApi.getSongListDetail(id, object : RequestCallBack<SongIdsJson> {
            override fun callback(data: SongIdsJson) {
                val listId = mutableListOf<Long>()
                //构建一个List，里面存放着歌单中的所有歌曲的id
                data.playlist?.trackIds?.forEach { trackId ->
                    trackId.id?.let {
                        listId.add(it)
                    }
                }

                //构建所有歌曲id的字符串.
                val builder = StringBuilder()
                for (i in 0 until listId.size) {
                    builder.append(listId[i])
                    if (i != (listId.size - 1)) {
                        builder.append(",")
                    }
                }

//                DataUtil.clientMusicApi.getSongsPlay(builder.toString(),
//                    object : RequestCallBack<SongPlayJson> {
//                        override fun callback(data: SongPlayJson) {
//                            data.data?.let {
//                                val songList = mutableListOf<Song>()
//                                for (i in 0 until it.size) {
//                                    songList.add(
//                                        Song(
//                                            it[i].id, it[i].url, it[i].size,
//                                            null, it[i].br, null, null
//                                        )
//                                    )
//                                }
//                                //回调出去
//                                listener.getPlayListDetailSuccess(songList)
//                            }
//                        }
//
//                        override fun error(errorMsg: String) {
//                            listener.error(errorMsg)
//                        }
//
//                    })
                DataUtil.clientMusicApi.getSongsDetail(builder.toString(),
                    object : RequestCallBack<SongDetailJson> {
                        override fun callback(data: SongDetailJson) {
                            data.songs?.let {
                                val songList = mutableListOf<Song>()
                                for (i in 0 until it.size) {
                                    val name: String = it[i].name ?: ""
                                    var picUrl: String = ""
                                    var artistName: String = ""
                                    it[i].al?.let { al ->
                                        picUrl = al.picUrl ?: ""
                                    }
                                    val builder = StringBuilder() //用于拼接歌手的名字.
                                    it[i].ar?.forEach { artist-> 
                                        builder.append("${artist.name} ")
                                    }
                                    artistName = builder.toString()
                                    songList.add(Song(null,null,null,name,null,artistName,picUrl))
                                }
                                listener.getPlayListDetailSuccess(songList)//回调出去
                            }
                        }

                        override fun error(errorMsg: String) {
                            listener.error(errorMsg)
                        }

                    })

            }

            override fun error(errorMsg: String) {
                listener.error(errorMsg)
            }

        })
    }
}