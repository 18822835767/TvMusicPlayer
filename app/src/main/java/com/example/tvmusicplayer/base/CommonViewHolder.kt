package com.example.tvmusicplayer.base

import android.util.SparseArray
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.R

class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    private var views : SparseArray<View> = SparseArray<View>()
    private var mItemView = itemView
    
    fun <T : View> getView(viewId : Int): View? {
        var view : View? = views.get(viewId)
        if(view == null){
            view = mItemView.findViewById(viewId)
            views.put(viewId,view)
        }
        return view as T
    }
    
    fun setText(viewId : Int,text : String) : CommonViewHolder{
        val textView : TextView = getView<TextView>(viewId) as TextView
        textView.setText(text)
        return this
    }
    
    fun setImageResource(viewId : Int,resId : Int) : CommonViewHolder{
        val imageView : ImageView = getView<ImageView>(resId) as ImageView
        imageView.setImageResource(resId)
        return this
    }
}