package com.example.repository.api

import com.example.repository.RequestCallBack
import com.example.repository.bean.HotList
import com.example.repository.bean.SearchDefaultJson
import com.example.repository.bean.SearchSongJson

interface ClientSearchApi {
    fun getSearchSongs(
        limit: Int,
        offset: Int,
        type: Int,
        keyword: String,
        callBack: RequestCallBack<SearchSongJson>
    )
    
    fun getDefaultKeywords(callBack: RequestCallBack<SearchDefaultJson>)

    /**
     * 获取热搜列表.
     * url：baseUrl/search/hot/detail
     * */
    fun getHotList(callBack: RequestCallBack<HotList>)
}