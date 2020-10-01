package com.example.tvmusicplayer.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.util.LogUtil

abstract class BaseRecyclerViewAdapter<T>(
    protected var data: MutableList<T>,
    private var itemLayoutId: Int
) : RecyclerView.Adapter<CommonViewHolder>() {

    private val TAG = "BaseRecyclerViewAdapter"
    private val TYPE_NORMAL: Int = 1
    private val TYPE_FOOTER: Int = 2
    
    private var listener: OnItemClickListener? = null

    private var footerView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        //该View是FooterView
        if (footerView != null && TYPE_FOOTER == viewType) {
            //如果footerView不为空，直接作为footerView使用
            footerView?.let {
                return CommonViewHolder(it)
            }
            //如果footer为空，那么使用一个默认的footerView.
            return CommonViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.footer_view,
                    parent, false
                )
            )
            //该view是正常的item
        } else {
            val view: View = LayoutInflater.from(parent.context).inflate(
                itemLayoutId, parent,
                false
            )
            return CommonViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        if (getItemViewType(position) != TYPE_FOOTER) {
            initItemView(holder, position)
            //添加点击监听
            holder.itemView.setOnClickListener { v -> listener?.onItemClick(v, position) }
        }
    }

    abstract fun initItemView(holder: CommonViewHolder, position: Int)

    override fun getItemCount(): Int {
        return if (footerView == null) data.size else data.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (footerView != null && position == itemCount - 1) {
            return TYPE_FOOTER
        } else {
            return TYPE_NORMAL
        }
    }

    fun setFooterView(footerView : View){
        this.footerView = footerView
//        notifyItemInserted(itemCount)
        notifyItemInserted(itemCount - 1)
    }
    
    fun removeFooterView(){
        notifyItemRemoved(itemCount - 1)
        footerView = null
    }
    
    fun setItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setDatas(datas: MutableList<T>) {
        data = datas
        notifyDataSetChanged()
    }

    fun addDatas(datas: MutableList<T>) {
        data.addAll(datas)
        notifyDataSetChanged()
    }

    fun clearAndAddNewDatas(datas: MutableList<T>) {
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T {
        return data[position]
    }

    fun getItems(): MutableList<T> {
        return data
    }

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }
} 