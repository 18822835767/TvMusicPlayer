package com.example.tvmusicplayer.model.impl

import com.example.data.DataUtil
import com.example.repository.RequestCallBack
import com.example.repository.bean.BannerJson
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
}