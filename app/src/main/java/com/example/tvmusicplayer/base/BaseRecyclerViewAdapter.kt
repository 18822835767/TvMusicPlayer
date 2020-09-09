package com.example.tvmusicplayer.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.R

abstract class BaseRecyclerViewAdapter<T>(protected var data: List<T>, private var itemLayoutId: Int)
    : RecyclerView.Adapter<CommonViewHolder>() {

    companion object {
        private val TYPE_OTHER: Int = 1
        private val TYPE_BOTTOM : Int = 2
    }

    /**
     * 表示是否底部有类似"加载更多"的东西.
     * */
    private var showBottom: Boolean = false
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
       if(TYPE_BOTTOM == viewType){
           return CommonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.footer_view,
               parent,false))
       }else{
           val view : View = LayoutInflater.from(parent.context).inflate(itemLayoutId,parent,
               false)
           return CommonViewHolder(view)
       }
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        if(getItemViewType(position) != TYPE_BOTTOM){
            initItemView(holder,position)   
        }else{
            //todo 处理bottom
        }
    }
    
    abstract fun initItemView(holder: CommonViewHolder,position: Int) 

    override fun getItemCount(): Int {
        if(showBottom){
            return data.size + 1
        }else{
            return data.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(!data.isEmpty() && data.size <= position){
            return TYPE_BOTTOM
        }else{
            return TYPE_OTHER
        }
    }
    
    fun setShowBottom(showBottom : Boolean){
        this.showBottom = showBottom
    }
    
    fun setDatas(datas : List<T>){
        data = datas
        notifyDataSetChanged()
    }
} 