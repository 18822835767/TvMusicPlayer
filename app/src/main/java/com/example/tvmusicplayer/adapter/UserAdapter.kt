package com.example.tvmusicplayer.adapter

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import kotlinx.android.synthetic.main.user_item.view.*

class UserAdapter(data : List<String>,itemLayoutId : Int) : BaseRecyclerViewAdapter<String>
    (data, itemLayoutId) {
    
    override fun initItemView(holder: CommonViewHolder, position: Int) {
        val str : String = data.get(position)
        holder.setText(R.id.text,str)
    }

}