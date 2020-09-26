package com.example.tvmusicplayer.adapter

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import com.example.tvmusicplayer.bean.Song

class PlayListDetailAdapter(data: MutableList<Song>, itemLayoutId: Int) :
    BaseRecyclerViewAdapter<Song>(data, itemLayoutId) {

    override fun initItemView(holder: CommonViewHolder, position: Int) {
        val song = data[position]
        holder.setText(R.id.song_name_tv, song.name ?: "")
        holder.setText(R.id.artist_name, song.artistName ?: "")
    }

    /**
     * 传入字符，获取list中首字母是该字母的item的位置.
     * */
    fun getSelectPosition(c: Char): Int {
        for (i in 0 until itemCount) {
            val firstLet: Char = data[i].firstLetter
            if (c == firstLet) {
                return i
            }
        }
        return -1
    }

}