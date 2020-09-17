package com.example.tvmusicplayer.adapter

import android.util.Log
import android.widget.ImageView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import com.example.tvmusicplayer.bean.PlayList
import com.squareup.picasso.Picasso

class UserPlayListAdapter(data : MutableList<PlayList>, itemLayoutId : Int) : BaseRecyclerViewAdapter<PlayList>
    (data,itemLayoutId) {
    override fun initItemView(holder: CommonViewHolder, position: Int) {
        val playList = data[position]
        val imageView = holder.getView<ImageView>(R.id.play_list_image)

        holder.setText(R.id.song_list_name,playList.name?:"XXX")
        
        Picasso.get().load(data[position].coverImgUrl)
            .placeholder(R.drawable.empty_photo)
            .error(R.drawable.load_error)
            .into(imageView as ImageView)
    }
}