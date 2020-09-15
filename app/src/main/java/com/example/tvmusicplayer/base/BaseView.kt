package com.example.tvmusicplayer.base

interface BaseView<P : BasePresenter> {
    fun setPresenter(presenter : P)
    
    fun showLoading()
    
    fun hideLoading()
    
    fun showError(errorMessage : String)
}