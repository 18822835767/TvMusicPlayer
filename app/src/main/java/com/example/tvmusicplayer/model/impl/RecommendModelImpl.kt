package com.example.tvmusicplayer.model.impl

import com.example.data.DataUtil
import com.example.repository.RequestCallBack
import com.example.repository.bean.BannerJson
import com.example.repository.bean.RecommendPlayList
import com.example.tvmusicplayer.bean.PlayList
import com.example.tvmusicplayer.model.RecommendModel

class RecommendModelImpl : RecommendModel{
    override fun getBanner(type: Int, listener: RecommendModel.OnListener) {
        DataUtil.clientImageApi.getBanners(type,object : RequestCallBack<BannerJson>{
            override fun callback(data: BannerJson) {
                val picUrl = mutableListOf<String>()
                data.banners?.forEach { 
                    it.pic?.let { pic-> 
                        picUrl.add(pic)
                    }
                }
                listener.getBannerSuccess(picUrl)
            }

            override fun error(errorMsg: String) {
                listener.error(errorMsg)
            }

        })
    }

    override fun getRecommendPlayList(limit: Int,listener: RecommendModel.OnListener) {
        DataUtil.clientMusicApi.getRecommendPlayList(limit,object : RequestCallBack<RecommendPlayList>{
            override fun callback(data: RecommendPlayList) {
                val playLists = mutableListOf<PlayList>()
                data.result?.let { 
                    it.forEach { elem -> 
                        val playList = PlayList(elem.id,elem.name,elem.picUrl)
                        playLists.add(playList)
                    }
                }
                listener.getRecommendPlayListSuccess(playLists)
            }

            override fun error(errorMsg: String) {
                listener.error(errorMsg)
            }

        })
    }
}