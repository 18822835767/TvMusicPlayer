package com.example.tvmusicplayer.base

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.R

abstract class BaseRecyclerViewAdapter<T>(protected var data: MutableList<T>, private var itemLayoutId: Int)
    : RecyclerView.Adapter<CommonViewHolder>() {

    private val TAG = "BaseRecyclerViewAdapter"
    
    companion object {
        private val TYPE_OTHER: Int = 1
        private val TYPE_BOTTOM : Int = 2
    }

    /**
     * 表示是否底部有类似"加载更多"的东西.
     * */
    protected var showBottom: Boolean = false
    
    private var listener : OnItemClickListener? = null
    
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
            //添加点击监听
            holder.itemView.setOnClickListener { v -> listener?.onItemClick(v,position) }
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
    
    fun setItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
    
    fun setDatas(datas : MutableList<T>){
        data = datas
        notifyDataSetChanged()
    }
    
    fun addDatas(datas: MutableList<T>){
        data.addAll(datas)
        notifyDataSetChanged()
    }
    
    fun getItem(position : Int): T {
        return data[position]
    }
    
    fun getItems(): MutableList<T> {
        return data
    }
    
    interface OnItemClickListener{
        fun onItemClick(v : View?,position : Int)
    }
} 