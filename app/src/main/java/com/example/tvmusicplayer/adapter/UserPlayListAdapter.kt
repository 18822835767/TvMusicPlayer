package com.example.tvmusicplayer.adapter

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import com.example.tvmusicplayer.bean.PlayList

class UserPlayListAdapter(data : List<PlayList>, itemLayoutId : Int) : BaseRecyclerViewAdapter<PlayList>
    (data,itemLayoutId) {
    override fun initItemView(holder: CommonViewHolder, position: Int) {
        val playList = data[position]
        holder.setText(R.id.song_list_name,playList.name?:"XXX")
    }
}