package com.example.tvmusicplayer.adapter

import android.widget.BaseAdapter
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.base.CommonViewHolder
import com.example.tvmusicplayer.util.Constant
import kotlinx.android.synthetic.main.user_item.view.*

class UserAdapter(data : MutableList<String>,itemLayoutId : Int) : BaseRecyclerViewAdapter<String>
    (data, itemLayoutId) {
    
    override fun initItemView(holder: CommonViewHolder, position: Int) {
        val str : String = data[position]
        holder.setText(R.id.text,str)
        when(str){
            Constant.LOCAL_MUSIC -> holder.setImageResource(R.id.image,R.drawable.local_music)
            Constant.DOWNLOAD_MANAGER -> holder.setImageResource(R.id.image,R.drawable.ic_download)
            Constant.MY_SONG_LIST -> holder.setImageResource(R.id.image,R.drawable.song_list)
        }
    }
    
}