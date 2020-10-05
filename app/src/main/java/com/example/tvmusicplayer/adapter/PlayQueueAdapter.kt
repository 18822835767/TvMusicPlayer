package com.example.tvmusicplayer.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import com.example.tvmusicplayer.bean.Song

class PlayQueueAdapter(data: MutableList<Song>, itemLayoutId: Int) :
    BaseRecyclerViewAdapter<Song>(data, itemLayoutId) {

    private var removeClickListener: OnRemoveClickListener? = null

    /**
     * 标记当前在播放的歌曲.
     * */
    private var curPosition: Int = -1

    override fun initItemView(holder: CommonViewHolder, position: Int) {
        val song = data[position]
        
        val songNameTv : TextView? = holder.getView<TextView>(R.id.song_name) as TextView?
        val singerNameTv : TextView? = holder.getView<TextView>(R.id.singer_name) as TextView?
        
        if(position != curPosition){
            songNameTv?.let{
                it.setTextColor(Color.GRAY)
                it.text = song.name ?: ""
            }

            singerNameTv?.let{
                it.setTextColor(Color.GRAY)
                it.text = song.artistName ?: ""
            }
        }else{
            songNameTv?.let{
                it.setTextColor(Color.RED)
                it.text = song.name ?: ""
            }

            singerNameTv?.let{
                it.setTextColor(Color.RED)
                it.text = song.artistName ?: ""
            }
        }
        
//        holder.setText(R.id.song_name, song.name ?: "")
//        holder.setText(R.id.singer_name, song.artistName ?: "")

        val removeView = holder.getView<ImageView>(R.id.remove)
        removeView?.let {
            it.setOnClickListener { v -> removeClickListener?.onRemoveClick(v, position) }
        }
    }

    fun setOnRemoveClickListener(listener: OnRemoveClickListener) {
        removeClickListener = listener
    }

    fun removeItem(position: Int) {
        data.removeAt(position)
        //todo 这个看下要不要优化一下
        notifyDataSetChanged()
    }

    interface OnRemoveClickListener {
        fun onRemoveClick(v: View?, position: Int)
    }
}