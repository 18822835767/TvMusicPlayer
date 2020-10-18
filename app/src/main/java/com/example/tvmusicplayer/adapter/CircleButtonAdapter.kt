package com.example.tvmusicplayer.adapter

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import com.example.tvmusicplayer.bean.CircleButtonBean

class CircleButtonAdapter(data : MutableList<CircleButtonBean>, itemLayoutId : Int) : 
    BaseRecyclerViewAdapter<CircleButtonBean>(data,itemLayoutId) {
    override fun initItemView(holder: CommonViewHolder, position: Int) {
        holder.setText(R.id.button_text,data[position].text?:"")
        holder.setImageResource(R.id.circle_button,data[position].imageResource?:R.drawable.ic_default)
    }
}