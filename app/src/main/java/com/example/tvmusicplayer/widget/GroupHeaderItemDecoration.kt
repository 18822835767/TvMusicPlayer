package com.example.tvmusicplayer.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.bean.Song

/**
 * 和字母栏一起使用的分割线，用于显示RecyclerView中每一组Item对应的字母.
 * */
class GroupHeaderItemDecoration(var songsList: MutableList<Song>) : RecyclerView.ItemDecoration() {
    
    private var groupHeaderHeight = 50
    private var groupHeaderLeftPadding = 5
    private var paint : Paint
    private var textPaint : TextPaint
    
    init {
        textPaint = TextPaint()
        textPaint.textSize = 15F
        textPaint.color = Color.BLACK
        
        paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.GRAY
    }
    
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        
        val position = parent.getChildAdapterPosition(view)
        if(position == 0 || (songsList[position].firstLetter != songsList[position - 1].firstLetter)){
            //如果是第一条Item 或者 不是第一条Item，但是该Item的首字母和前一个Item不同
            outRect.set(0,groupHeaderHeight,0,0)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        
        for(i in 0 until parent.childCount){
            val view : View = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            val letter = songsList[position].firstLetter.toString()
            if(position == 0 || (songsList[position].firstLetter != songsList[position - 1].firstLetter)){
                drawGroupHeader(c,parent,view,letter)
            }
        }
    }
    
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }
    
    private fun drawGroupHeader(c : Canvas,parent : RecyclerView,view : View, letter : String){
        val params : RecyclerView.LayoutParams = view.layoutParams as RecyclerView.LayoutParams
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val bottom = view.top - params.topMargin
        val top = bottom - groupHeaderHeight
        c.drawRect(left.toFloat(),top.toFloat(),right.toFloat(),bottom.toFloat(),paint)
        val x = left + groupHeaderLeftPadding
        val y = top + (groupHeaderHeight + textPaint.measureText(letter)) / 2
        c.drawText(letter,x.toFloat(),y,textPaint)
    }
}