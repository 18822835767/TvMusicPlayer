package com.example.tvmusicplayer.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.Scroller
import com.example.tvmusicplayer.bean.Lyrics

/**
 * 显示歌词的控件.
 * */
class LrcView : View {

    companion object {
        //滚动时间
        val SCROLL_TIME = 500

        //无歌词时的默认显示
        val DEFAUKT_TEXT = "暂无歌词"
    }

    /**
     * 存放歌词实体类.
     * */
    private val lryList = mutableListOf<Lyrics>()

    /**
     * 下一句开始时间.
     * */
    private var nextTime = 0L

    /**
     * 宽度.
     * */
    private var viewWidth: Int = 0

    /**
     * 歌词界面的高度.
     * */
    private var lryHeight: Int = 0

    /**
     * 记录展示多少行.
     * */
    private var rows: Int = 7

    /**
     * 记录当前展示、高亮的行(在list中的下标).
     * */
    private var currentLine = 0

    /**
     * 保存滚动时，y方向上的偏移.
     * */
    private var offSetY: Float = 0F

    /**
     * 用于保存行与行切换时，应该最大滚动的距离 = 一行歌词高度+歌词间距.
     * */
    private var maxScroll: Int = 0

    /**
     * 记录字体大小
     * */
    private var textSize: Float = 50F

    /**
     * 行间距.
     * */
    private var dividerHeight: Float = 20F

    private var textBounds: Rect = Rect()

    /**
     * 常规字体的画笔.
     * */
    private var normalPaint= Paint()

    /**
     * 高亮行的画笔.
     * */
    private var currentPaint: Paint = Paint()
    
    private var scroller : Scroller? = null 
    
    constructor(context: Context?) : super(context) {
        initData()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initData()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initData()
    }

    private fun initData(){
        //初始化画笔
        normalPaint = Paint()
        currentPaint = Paint()
        
        normalPaint.textSize = textSize
        normalPaint.color = Color.WHITE
        normalPaint.isAntiAlias = true
        currentPaint.textSize = textSize
        currentPaint.color = Color.GREEN
        currentPaint.isAntiAlias = true
        
        currentPaint .getTextBounds(DEFAUKT_TEXT,0, DEFAUKT_TEXT.length,textBounds)
        maxScroll = (textBounds.height() + dividerHeight).toInt()
    }

}