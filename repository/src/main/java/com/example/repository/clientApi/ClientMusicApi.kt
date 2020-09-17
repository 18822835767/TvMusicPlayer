package com.example.repository.clientApi

import com.example.repository.GsonBean.UserPlayListJson
import com.example.repository.callback.CallBack
import retrofit2.http.GET

/**
 * 暴露给用户使用的，与音乐有关的Api.
 * */
interface ClientMusicApi {
    fun getUserPlayList(uid : Long,callBack: CallBack<UserPlayListJson>)
}