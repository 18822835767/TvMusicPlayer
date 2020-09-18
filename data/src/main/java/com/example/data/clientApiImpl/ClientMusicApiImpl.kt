package com.example.data.clientApiImpl

import android.annotation.SuppressLint
import com.example.data.DataUtil
import com.example.data.RequestCallBack
import com.example.data.api.ClientMusicApi
import com.example.data.bean.UserPlayListJson
import com.example.data.util.LogUtil
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class ClientMusicApiImpl : ClientMusicApi {
    private val TAG = "ClientMusicApiImpl"

    override fun getUserPlayList(uid: Long, callback: RequestCallBack<UserPlayListJson>) {
        DataUtil.observableMusicApi.getUserPlayList(uid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UserPlayListJson> {
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: UserPlayListJson) {
                    LogUtil.d(TAG, "onNext")
                    callback.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callback.error(e.message?:"UnKnown_error")
                }

            })
    }

}