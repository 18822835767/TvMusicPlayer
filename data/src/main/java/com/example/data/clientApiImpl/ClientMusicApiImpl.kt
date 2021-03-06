package com.example.data.clientApiImpl

import android.annotation.SuppressLint
import com.example.data.DataUtil
import com.example.repository.RequestCallBack
import com.example.repository.api.ClientMusicApi
import com.example.data.util.LogUtil
import com.example.repository.bean.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class ClientMusicApiImpl : ClientMusicApi {
    private val TAG = "ClientMusicApiImpl"

    override fun getUserPlayList(uid: Long, callBack: RequestCallBack<UserPlayListJson>) {
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
                    callBack.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callBack.error(e.message ?: "UnKnown_error")
                }

            })
    }

    override fun getSongListDetail(id: Long, callBack: RequestCallBack<SongIdsJson>) {
        DataUtil.observableMusicApi.getSongListDetail(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SongIdsJson> {
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: SongIdsJson) {
                    LogUtil.d(TAG, "onNext")
                    callBack.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callBack.error(e.message ?: "UnKnown_error")
                }

            })
    }

    override fun getSongsDetail(ids: String, callBack: RequestCallBack<SongDetailJson>) {
        DataUtil.observableMusicApi.getSongsDetail(ids)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SongDetailJson> {
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: SongDetailJson) {
                    LogUtil.d(TAG, "onNext")
                    callBack.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callBack.error(e.message ?: "UnKnown_error")
                }

            })
    }

    override fun getSongPlay(id: Long, callBack: RequestCallBack<SongPlayJson>) {
        DataUtil.observableMusicApi.getSongPlay(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SongPlayJson> {
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: SongPlayJson) {
                    LogUtil.d(TAG, "onNext")
                    callBack.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callBack.error(e.message ?: "UnKnown_error")
                }

            })
    }

    override fun getSongsPlay(ids: String, callBack: RequestCallBack<SongPlayJson>) {
        DataUtil.observableMusicApi.getSongsPlay(ids)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SongPlayJson> {
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: SongPlayJson) {
                    LogUtil.d(TAG, "onNext")
                    callBack.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callBack.error(e.message ?: "UnKnown_error")
                }
            })
    }

    override fun getSongLyric(id: Long, callBack: RequestCallBack<LyricJson>) {
        DataUtil.observableMusicApi.getSongLyric(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<LyricJson>{
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: LyricJson) {
                    LogUtil.d(TAG, "onNext")
                    callBack.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callBack.error(e.message ?: "UnKnown_error")
                }

            })
    }

    override fun getRecommendPlayList(limit: Int, callBack: RequestCallBack<RecommendPlayList>) {
        DataUtil.observableMusicApi.getRecommendPlayList(limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<RecommendPlayList>{
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: RecommendPlayList) {
                    LogUtil.d(TAG, "onNext")
                    callBack.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callBack.error(e.message ?: "UnKnown_error")
                }
            })
    }

    override fun getDailyRecommendPlayList(callback: RequestCallBack<DailyRecommendPlayList>) {
        DataUtil.observableMusicApi.getDailyRecommendPlayList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DailyRecommendPlayList>{
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: DailyRecommendPlayList) {
                    LogUtil.d(TAG, "onNext")
                    callback.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callback.error(e.message ?: "UnKnown_error")
                }

            })
    }

    override fun getRecommendNewSong(callback : RequestCallBack<RecommendNewSong>) {
        DataUtil.observableMusicApi.getRecommendNewSong()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<RecommendNewSong>{
                override fun onComplete() {
                    LogUtil.d(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtil.d(TAG, "onSubscribe")
                }

                override fun onNext(t: RecommendNewSong) {
                    LogUtil.d(TAG, "onNext")
                    callback.callback(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.d(TAG, "onError" + e.message)
                    callback.error(e.message ?: "UnKnown_error")
                }

            })
    }

}