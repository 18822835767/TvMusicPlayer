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

    private val songList = mutableListOf<Song>()

    /**
     * 构建所有歌曲id的字符串.
     * */
    private val idsBuilder = StringBuilder()

    override fun getPlayListDetail(id: Long, listener: PlayListDetailModel.OnListener) {
        songList.clear()//清空数据
        idsBuilder.clear()

        DataUtil.clientMusicApi.getSongListDetail(id, object : RequestCallBack<SongIdsJson> {
            override fun callback(data: SongIdsJson) {
                val listId = mutableListOf<Long>()
                //构建一个List，里面存放着歌单中的所有歌曲的id
                data.playlist?.trackIds?.forEach { trackId ->
                    trackId.id?.let {
                        listId.add(it)
                    }
                }

                for (i in 0 until listId.size) {
                    idsBuilder.append(listId[i])
                    if (i != (listId.size - 1)) {
                        idsBuilder.append(",")
                    }
                    songList.add(
                        Song(
                            listId[i], null, null, null, null,
                            null, null
                        )
                    )
                }
                getSongDetail(listener)
            }

            override fun error(errorMsg: String) {
                listener.error(errorMsg)
            }

        })
    }

    /**
     * 获取音乐详情，包括歌曲名字，歌手名字等等.
     * */
    private fun getSongDetail(listener: PlayListDetailModel.OnListener) {
        DataUtil.clientMusicApi.getSongsDetail(idsBuilder.toString(),
            object : RequestCallBack<SongDetailJson> {
                override fun callback(data: SongDetailJson) {
                    data.songs?.let {
                        for (i in 0 until it.size) {
                            val name: String = it[i].name ?: ""
                            var picUrl = ""
                            var artistName = ""
                            it[i].al?.let { al ->
                                picUrl = al.picUrl ?: ""
                            }
                            val singerBuilder = StringBuilder() //用于拼接歌手的名字.
                            it[i].ar?.forEach { artist ->
                                singerBuilder.append("${artist.name} ")
                            }
                            artistName = singerBuilder.toString()
                            //从list中获取相应的Song实体
                            val song = songList[i]
                            song.name = name
                            song.artistName = artistName
                            song.picUrl = picUrl
                        }
                        listener.getPlayListDetailSuccess(songList)//回调出去
//                        getSongsPlayInfo(listener)
                    }
                }

                override fun error(errorMsg: String) {
                    listener.error(errorMsg)
                }

            })
    }

    /**
     * 获取音乐播放相关的信息，例如url、size等等.
     * 这里接口有问题，获取的数据不全，最后一小部分数据不正常.
     * */
    private fun getSongsPlayInfo(listener: PlayListDetailModel.OnListener) {
        DataUtil.clientMusicApi.getSongsPlay(idsBuilder.toString(), object :
            RequestCallBack<SongPlayJson> {
            override fun callback(data: SongPlayJson) {
                data.data?.let {
                    for (i in 0 until it.size) {
                        val song = songList[i]
                        song.url = it[i].url
                        song.size = it[i].size
                        song.br = it[i].br
                    }
                    //回调出去
                    listener.getPlayListDetailSuccess(songList)
                }
            }

            override fun error(errorMsg: String) {
                listener.error(errorMsg)
            }

        })
    }
}