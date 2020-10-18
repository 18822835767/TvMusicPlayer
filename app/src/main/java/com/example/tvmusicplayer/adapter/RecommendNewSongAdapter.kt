package com.example.tvmusicplayer.adapter

import android.widget.ImageView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import com.example.tvmusicplayer.bean.Song
import com.squareup.picasso.Picasso

class RecommendNewSongAdapter(data : MutableList<Song>,itemLayoutId : Int) : 
    BaseRecyclerViewAdapter<Song>(data,itemLayoutId){
    override fun initItemView(holder: CommonViewHolder, position: Int) {
        val song = data[position]
        val imageView = holder.getView<ImageView>(R.id.cover_iv)
        holder.setText(R.id.song_name_tv,song.name?:"")
        holder.setText(R.id.singer_name_tv,song.artistName?:"")
        
        Picasso.get().load(song.picUrl)
            .resize(90,90)
            .placeholder(R.drawable.empty_photo)
            .error(R.drawable.load_error)
            .into(imageView as ImageView)
    }

}