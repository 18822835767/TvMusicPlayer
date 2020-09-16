package com.example.repository.clientApi.impl

import android.annotation.SuppressLint
import android.util.Log
import com.example.repository.DataUtil
import com.example.repository.GsonBean.UserJson
import com.example.repository.callback.CallBack
import com.example.repository.clientApi.ClientLoginApi
import com.example.repository.util.LogUtil
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class ClientLoginApiImpl : ClientLoginApi{
    val TAG = "ClientLoginApiImpl"
    
    override fun login(username: String, password: String, callback : CallBack<UserJson>){
        DataUtil.observableLoginApi.login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UserJson>{
                override fun onComplete() {
                    LogUtil.d(TAG,"onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG,"onSubscribe")
                }

                override fun onNext(t: UserJson) {
                    LogUtil.d(TAG,"onNext")
                    callback.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG,"onError"+e.message)
                    callback.error(e.message?:"error")
                }

            })
    }

}