package com.example.tvmusicplayer.model.impl

import com.example.tvmusicplayer.model.LocalModel
import com.example.tvmusicplayer.util.LocalSongsUtil

class LocalModelImpl : LocalModel {
    override fun getLocalSongs(listener: LocalModel.OnListener) {
        listener.getLocalSongsSuccess(LocalSongsUtil.getLocalSong())
    }
}