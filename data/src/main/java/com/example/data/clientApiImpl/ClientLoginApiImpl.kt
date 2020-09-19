package com.example.data.clientApiImpl

import android.annotation.SuppressLint
import com.example.data.DataUtil
import com.example.repository.RequestCallBack
import com.example.repository.api.ClientLoginApi
import com.example.repository.bean.UserJson
import com.example.data.util.LogUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class ClientLoginApiImpl : ClientLoginApi {
    private val TAG = "ClientLoginApiImpl"

    override fun login(username: String, password: String, callback: RequestCallBack<UserJson>) {
        DataUtil.observableLoginApi.login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UserJson> {
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: UserJson) {
                    LogUtil.d(TAG, "onNext")
                    callback.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callback.error(e.message ?: "UnKnown_error")
                }

            })
    }

    override fun getLoginStatus(callback: RequestCallBack<UserJson>) {
        DataUtil.observableLoginApi.getLoginStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UserJson> {
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: UserJson) {
                    LogUtil.d(TAG, "onNext")
                    callback.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callback.error(e.message ?: "UnKnown_error")
                }

            })
    }

    override fun logout() {
        DataUtil.observableLoginApi.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Unit>{
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: Unit) {
                    LogUtil.d(TAG, "onNext")
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                }

            })
            
    }

}