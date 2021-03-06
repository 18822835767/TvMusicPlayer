package com.example.tvmusicplayer.recommend

import com.example.tvmusicplayer.bean.PlayList
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.model.RecommendModel
import com.example.tvmusicplayer.model.impl.RecommendModelImpl

class RecommendPresenter(var onView : RecommendContract.OnView) : RecommendContract.Presenter,
    RecommendModel.OnListener{
    
    private val model :RecommendModel
    
    init {
        onView.setPresenter(this)
        model = RecommendModelImpl()
    }
    
    override fun getBanner(type :Int) {
        model.getBanner(type,this)
    }

    override fun getRecommendPlayList(limit: Int) {
        model.getRecommendPlayList(limit,this)
    }

    override fun getRecommendNewSong() {
        model.getRecommendNewSong(this)
    }

    override fun start() {
    }

    override fun getBannerSuccess(list: MutableList<String>) {
        onView.getBannerSuccess(list)
    }

    override fun getRecommendPlayListSuccess(list: MutableList<PlayList>) {
        onView.getRecommendPlayListSuccess(list)
    }

    override fun getRecommendNewSongSuccess(list: MutableList<Song>) {
        onView.getRecommendNewSongSuccess(list)
    }

    override fun error(msg: String) {
        onView.showError(msg)
    }

}