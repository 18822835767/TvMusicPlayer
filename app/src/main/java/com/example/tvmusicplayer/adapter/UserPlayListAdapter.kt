package com.example.tvmusicplayer.adapter

import android.util.Log
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import com.example.tvmusicplayer.bean.PlayList

class UserPlayListAdapter(data : MutableList<PlayList>, itemLayoutId : Int) : BaseRecyclerViewAdapter<PlayList>
    (data,itemLayoutId) {
    override fun initItemView(holder: CommonViewHolder, position: Int) {
        val playList = data[position]
        holder.setText(R.id.song_list_name,playList.name?:"XXX")
    }
}