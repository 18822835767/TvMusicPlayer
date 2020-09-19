package com.example.tvmusicplayer.model.impl

import com.example.data.DataUtil
import com.example.repository.RequestCallBack
import com.example.repository.bean.UserPlayListJson
import com.example.tvmusicplayer.bean.PlayList
import com.example.tvmusicplayer.model.UserPlayListModel
import com.example.tvmusicplayer.util.LogUtil

class UserPlayListModelImpl : UserPlayListModel{
    private val TAG = "UserPlayListModelImpl"
    
    override fun getUserPlayList(uid: Long, listener: UserPlayListModel.OnListener) {
        DataUtil.clientMusicApi.getUserPlayList(uid,object :
            RequestCallBack<UserPlayListJson> {
            override fun callback(data: UserPlayListJson) {
                
                //解析数据为所需的实体类
                data.playlist?.let {
                    val playLists = mutableListOf<PlayList>()
                    for(i in 0 until it.size){
                        playLists.add(PlayList(it[i].id,it[i].name,it[i].coverImgUrl))
                    }
                    //回调给P层
                    listener.getUserPlayListSuccess(playLists)
                }
                
            }

            override fun error(errorMsg: String) {
                LogUtil.d(TAG,errorMsg)
                listener.error(errorMsg)
            }

        })
    }
}