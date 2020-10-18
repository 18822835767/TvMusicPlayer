package com.example.tvmusicplayer.adapter

import android.view.View
import android.widget.ImageView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import com.example.tvmusicplayer.bean.Song

class LocalAdapter(data: MutableList<Song>, itemLayoutId: Int) :
    BaseRecyclerViewAdapter<Song>(data, itemLayoutId) {

    private var popupClickListener: OnPopupClickListener? = null

    override fun initItemView(holder: CommonViewHolder, position: Int) {
        val song = data[position]
        holder.setText(R.id.song_name_tv, song.name ?: "")
        holder.setText(R.id.artist_name, song.artistName ?: "")

        //为popup增加监听.
        val popupView = holder.getView<ImageView>(R.id.popup)
        popupView?.let {
            it.setOnClickListener { v -> popupClickListener?.onPopupClick(v, position) }
        }
    }

    fun setOnPopupClickListener(listener: OnPopupClickListener) {
        popupClickListener = listener
    }

    interface OnPopupClickListener {
        fun onPopupClick(v: View?, position: Int)
    }
}