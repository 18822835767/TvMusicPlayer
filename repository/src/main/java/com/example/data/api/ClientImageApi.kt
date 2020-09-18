package com.example.data.api

import com.example.data.RequestCallBack
import com.example.data.bean.BannerJson

interface ClientImageApi {
    /**
     * 获取轮播图.
     * */
    fun getBanners(type : Int ,callBack: RequestCallBack<BannerJson>)
}