package com.example.tvmusicplayer.adapter

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import com.example.tvmusicplayer.bean.Song
import kotlinx.android.synthetic.main.song_item.view.*

class PlayListDetailAdapter(data : MutableList<Song>,itemLayoutId : Int) : 
    BaseRecyclerViewAdapter<Song>(data,itemLayoutId) {
    
    override fun initItemView(holder: CommonViewHolder, position: Int) {
        val song = data[position]
        holder.setText(R.id.music_name,song.name?:"")
        holder.setText(R.id.artist_name,song.artistName?:"")
    }

}