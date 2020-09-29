package com.example.tvmusicplayer.model.impl

import com.example.data.DataUtil
import com.example.repository.RequestCallBack
import com.example.repository.bean.SearchSongJson
import com.example.repository.bean.SongDetailJson
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.model.PlayListDetailModel
import com.example.tvmusicplayer.model.SearchModel
import com.example.tvmusicplayer.util.PinyinUtil
import java.lang.StringBuilder

class SearchModelImpl : SearchModel{

    private val songList = mutableListOf<Song>()
    
    /**
     * 构建所有歌曲id的字符串.
     * */
    private val idsBuilder = StringBuilder()
    
    override fun searchSongs(
        limit: Int,
        offset: Int,
        type: Int,
        keyword: String,
        listener: SearchModel.OnListener
    ) {
        songList.clear()//清空数据
        idsBuilder.clear()
        
        DataUtil.clientSearchApi.getSearchSongs(limit,offset,type,keyword,object : RequestCallBack<SearchSongJson>{
            override fun callback(data: SearchSongJson) {
                val listId = mutableListOf<Long>()
                //构建一个List，里面存放着歌单中的所有歌曲的id
                data.result?.songs?.forEach {songJson ->
                    songJson.id?.let { 
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
     * 获取音乐详情，这里主要是为了获取歌曲专辑的封面图片url.
     * */
    private fun getSongDetail(listener: SearchModel.OnListener) {
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

                            if(name.isNotEmpty()){
                                song.firstLetter = PinyinUtil.getHeaderLetter(name)
                            }else{
                                song.firstLetter = '#'
                            }
                        }
                        listener.searchSuccess(songList)//回调出去
//                        getSongsPlayInfo(listener)
                    }
                }

                override fun error(errorMsg: String) {
                    listener.error(errorMsg)
                }

            })
    }

}