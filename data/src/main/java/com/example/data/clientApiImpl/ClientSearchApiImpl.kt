package com.example.data.clientApiImpl

import android.annotation.SuppressLint
import com.example.data.DataUtil
import com.example.data.util.LogUtil
import com.example.repository.RequestCallBack
import com.example.repository.api.ClientSearchApi
import com.example.repository.bean.SearchDefaultJson
import com.example.repository.bean.SearchSongJson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class ClientSearchApiImpl : ClientSearchApi{
    private val TAG = "ClientSearchApiImpl"
    
    override fun getSearchSongs(
        limit: Int,
        offset: Int,
        type: Int,
        keyword: String,
        callBack: RequestCallBack<SearchSongJson>
    ) {
        DataUtil.observableSearchApi.searchMusics(limit,offset,type,keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SearchSongJson>{
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: SearchSongJson) {
                    LogUtil.d(TAG, "onNext")
                    callBack.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callBack.error(e.message ?: "UnKnown_error")
                }

            })
        
    }

    override fun getDefaultKeywords(callBack: RequestCallBack<SearchDefaultJson>) {
        TODO("Not yet implemented")
    }

}