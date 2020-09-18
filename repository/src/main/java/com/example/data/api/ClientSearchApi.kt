package com.example.data.api

import com.example.data.RequestCallBack
import com.example.data.bean.SearchDefaultJson
import com.example.data.bean.SearchSongJson

interface ClientSearchApi {
    fun getSearchSongs(
        limit: Int,
        offset: Int,
        type: Int,
        keyword: String,
        callBack: RequestCallBack<SearchSongJson>
    )
    
    fun getDefaultKeywords(callBack: RequestCallBack<SearchDefaultJson>)
}