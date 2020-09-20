package com.example.tvmusicplayer.model.impl

import com.example.data.DataUtil
import com.example.repository.RequestCallBack
import com.example.repository.bean.SongIdsJson
import com.example.tvmusicplayer.model.PlayListDetailModel

class PlayListDetailModelImpl : PlayListDetailModel{
    override fun getPlayListDetail(id: Long,listener : PlayListDetailModel.OnListener) {
        DataUtil.clientMusicApi.getSongListDetail(id,object : RequestCallBack<SongIdsJson>{
            override fun callback(data: SongIdsJson) {
                
            }

            override fun error(errorMsg: String) {
                listener.error(errorMsg)
            }

        })   
    }
}