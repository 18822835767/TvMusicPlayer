package com.example.tvmusicplayer.base

interface BaseView<P : BasePresenter> {
    fun setPresenter(presenter : P)
}