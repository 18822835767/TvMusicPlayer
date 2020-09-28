package com.example.tvmusicplayer.widget

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.bean.Song

/**
 * 和字母栏一起使用的分割线，用于显示RecyclerView中每一组Item对应的字母.
 * */
class GroupHeaderItemDecoration(var songsList: MutableList<Song>) : RecyclerView.ItemDecoration() {
    
    private var groupHeaderHeight = 50
    
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        
        val position = parent.getChildAdapterPosition(view)
        if(position == 0){
            //如果是第一条Item
            outRect.set(0,groupHeaderHeight,0,0)
        }else if(songsList[position].firstLetter != songsList[position - 1].firstLetter){
            //如果不是第一条Item，并且该Item的首字母和前一个Item不同
            outRect.set(0,groupHeaderHeight,0,0)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }
}