package com.example.tvmusicplayer.adapter

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import kotlinx.android.synthetic.main.circle_button_item.view.*

class CircleButtonAdapter(data : MutableList<String>,itemLayoutId : Int) : 
    BaseRecyclerViewAdapter<String>(data,itemLayoutId) {
    override fun initItemView(holder: CommonViewHolder, position: Int) {
        holder.setText(R.id.button_text,data[position])   
    }
}