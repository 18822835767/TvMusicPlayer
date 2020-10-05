package com.example.tvmusicplayer.adapter

import android.view.View
import android.widget.ImageView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import com.example.tvmusicplayer.bean.Song

class PlayQueueAdapter(data : MutableList<Song>,itemLayoutId : Int) : 
    BaseRecyclerViewAdapter<Song>(data,itemLayoutId) {
    
    private var removeClickListener : OnRemoveClickListener? = null 
    
    override fun initItemView(holder: CommonViewHolder, position: Int) {
        val song = data[position]
        holder.setText(R.id.song_name,song.name?:"")
        holder.setText(R.id.singer_name,song.artistName?:"")
    
        val removeView = holder.getView<ImageView>(R.id.remove)
        removeView?.let{
            it.setOnClickListener{v -> removeClickListener?.onRemoveClick(v,position)}
        }
    }
    
    fun setOnRemoveClickListener(listener: OnRemoveClickListener){
        removeClickListener = listener
    }
    
    fun removeItem(position: Int){
        data.removeAt(position)
        //todo 这个看下要不要优化一下
        notifyDataSetChanged()
    }
    
    interface OnRemoveClickListener{
        fun onRemoveClick(v: View?, position: Int)
    }
}