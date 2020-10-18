package com.example.tvmusicplayer.adapter

import android.widget.ImageView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import com.example.tvmusicplayer.bean.PlayList
import com.squareup.picasso.Picasso

class RecommendPlayListAdapter(data : MutableList<PlayList>,itemLayoutId : Int) : 
    BaseRecyclerViewAdapter<PlayList>(data,itemLayoutId){
    override fun initItemView(holder: CommonViewHolder, position: Int) {
        val playList = data[position]
        val imageView = holder.getView<ImageView>(R.id.cover_iv)
        holder.setText(R.id.name_tv,playList.name?:"")
        
        Picasso.get().load(playList.coverImgUrl)
            .resize(90,90)
            .placeholder(R.drawable.empty_photo)
            .error(R.drawable.load_error)
            .into(imageView as ImageView)
    }

}