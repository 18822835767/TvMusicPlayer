package com.example.tvmusicplayer.search

import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.model.SearchModel
import com.example.tvmusicplayer.model.impl.SearchModelImpl

class SearchPresenter(var onView: SearchContract.OnView) : SearchContract.Presenter,
    SearchModel.OnListener {

    private val model: SearchModel

    init {
        onView.setPresenter(this)
        model = SearchModelImpl()
    }

    override fun searchSongs(limit: Int, offset: Int, type: Int, keyword: String) {
        onView.showLoading()
        model.searchOrLoadMoreSongs(limit, offset, type, keyword, this)
    }

    override fun loadMoreSongs(limit: Int, offset: Int, type: Int, keyword: String) {
        model.searchOrLoadMoreSongs(limit, offset, type, keyword, this)
    }

    override fun start() {
    }

    override fun searchSuccess(list: MutableList<Song>, songCount: Int) {
        onView.hideLoading()
        onView.searchSuccess(list, songCount)
    }

    override fun loadMoreSuccess(list: MutableList<Song>, songCount: Int) {
        onView.loadMoreSuccess(list, songCount)
    }

    override fun searchError(msg: String) {
        onView.hideLoading()
        onView.searchError(msg)
    }

    override fun loadMoreError(msg: String) {
        onView.loadMoreError(msg)
    }

    /**
     * 该方法在这里暂时没用，上面有两个方法分别对两种不同的error进行了处理.
     * */
    override fun error(msg: String) {
//        onView.hideLoading()
//        onView.showError(msg)
    }
}