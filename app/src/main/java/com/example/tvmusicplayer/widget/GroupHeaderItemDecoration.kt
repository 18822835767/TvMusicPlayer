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
import com.example.tvmusicplayer.util.LogUtil

/**
 * 和字母栏一起使用的分割线，用于显示RecyclerView中每一组Item对应的字母.
 * */
class GroupHeaderItemDecoration(var songsList: MutableList<Song>) : RecyclerView.ItemDecoration() {

    private var groupHeaderHeight = 90
    private var groupHeaderLeftPadding = 20
    private var paint: Paint
    private var textPaint: TextPaint
    private var dividerPaint: Paint
    private var dividerHeight = 1

    init {
        textPaint = TextPaint()
        textPaint.textSize = 55F
        textPaint.color = Color.BLACK

        paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#E6E6FA")

        dividerPaint = Paint()
        dividerPaint.style = Paint.Style.FILL_AND_STROKE
        dividerPaint.color = Color.GRAY
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        if (position == 0 || (songsList[position].firstLetter != songsList[position - 1].firstLetter)) {
            //如果是第一条Item 或者 不是第一条Item，但是该Item的首字母和前一个Item不同
            outRect.set(0, groupHeaderHeight, 0, 0)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        //parent.childCount是显示在屏幕内的item数量
        for (i in 0 until parent.childCount) {
            val view: View = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            val letter = songsList[position].firstLetter.toString()
            //第一条item 或者 这条item字母和前一条item的字母不同，那么就画字母
            if (position == 0 || (songsList[position].firstLetter != songsList[position - 1].firstLetter)) {
                drawGroupHeader(c, parent, view, letter)
            }
            
            //画分割线，画的条件是 不是最后一条 并且 该item的字母和下一个item的字母是相同的
            if(position + 1 < songsList.size && songsList[position].firstLetter == songsList[position + 1].firstLetter){
                drawDivider(c,parent,view)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }

    /**
     * 画每组Item上的字母.
     * */
    private fun drawGroupHeader(c: Canvas, parent: RecyclerView, view: View, letter: String) {
        val params: RecyclerView.LayoutParams = view.layoutParams as RecyclerView.LayoutParams
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val bottom = view.top - params.topMargin
        val top = bottom - groupHeaderHeight
        //先画一个矩形
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        val x = left + groupHeaderLeftPadding
        val y = top + (groupHeaderHeight + textPaint.measureText(letter)) / 2
        //在矩形中画字母
        c.drawText(letter, x.toFloat(), y, textPaint)
    }

    /**
     * 画分割线.
     * */
    private fun drawDivider(c : Canvas,parent : RecyclerView,view : View) {
        val params : RecyclerView.LayoutParams = view.layoutParams as RecyclerView.LayoutParams
        val left = parent.paddingLeft
        val right = parent.width + parent.paddingRight
        val top = view.bottom + params.bottomMargin
        val bottom = top + dividerHeight
        //画一个分割线
        c.drawRect(left.toFloat(),top.toFloat(),right.toFloat(),bottom.toFloat(),dividerPaint)
    }
}