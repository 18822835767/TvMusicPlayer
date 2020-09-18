package com.example.repository.api

import com.example.repository.bean.BannerJson
import com.example.repository.callback.RequestCallBack

interface ClientImageApi {
    /**
     * 获取轮播图.
     * */
    fun getBanners(type : Int ,callBack: RequestCallBack<BannerJson>)
}