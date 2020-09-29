package com.example.data.clientApiImpl

import com.example.repository.RequestCallBack
import com.example.repository.api.ClientSearchApi
import com.example.repository.bean.SearchDefaultJson
import com.example.repository.bean.SearchSongJson

class ClientSearchApiImpl : ClientSearchApi{
    override fun getSearchSongs(
        limit: Int,
        offset: Int,
        type: Int,
        keyword: String,
        callBack: RequestCallBack<SearchSongJson>
    ) {
        TODO("Not yet implemented")
    }

    override fun getDefaultKeywords(callBack: RequestCallBack<SearchDefaultJson>) {
        TODO("Not yet implemented")
    }

}