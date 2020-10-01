package com.example.tvmusicplayer.base

interface BaseView<P : BasePresenter> {
    fun setPresenter(presenter: P)

    fun showLoading()

    fun hideLoading()

    /**
     * 这个方法也可以考虑不要设计到通用接口的方法里，参考BaseModelListener的注释解释.
     * */
    fun showError(errorMessage: String)
}