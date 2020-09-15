package com.example.repository.clientApi.impl

import android.annotation.SuppressLint
import com.example.repository.DataUtil
import com.example.repository.GsonBean.UserJson
import com.example.repository.callback.CallBack
import com.example.repository.clientApi.ClientLoginApi
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class ClientLoginApiImpl : ClientLoginApi{
    override fun login(username: String, password: String, callback : CallBack<UserJson>){
        DataUtil.observableLoginApi.login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UserJson>{
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: UserJson) {
                    callback.callback(t)
                }

                override fun onError(e: Throwable) {
                }

            })
    }

}