package com.example.data.clientApiImpl

import android.annotation.SuppressLint
import com.example.data.DataUtil
import com.example.data.util.LogUtil
import com.example.repository.RequestCallBack
import com.example.repository.api.ClientImageApi
import com.example.repository.bean.BannerJson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class ClientImageApiImpl : ClientImageApi{
    private val TAG = "ClientImageApiImpl"
    
    override fun getBanners(type: Int, callBack: RequestCallBack<BannerJson>) {
        DataUtil.observableImageApi.getBanners(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BannerJson>{
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: BannerJson) {
                    LogUtil.d(TAG, "onNext")
                    callBack.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callBack.error(e.message ?: "UnKnown_error")
                }

            })
    }

}