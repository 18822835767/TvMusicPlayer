package com.example.tvmusicplayer.widget

import android.content.Context
import android.graphics.Canvas
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
    private var viewWidth: Int = 200

    /**
     * 歌词界面的高度.
     * */
    private var lrcHeight: Int = 0

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
    private var normalPaint = Paint()

    /**
     * 高亮行的画笔.
     * */
    private var currentPaint: Paint = Paint()

    private var scroller: Scroller? = null

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

    private fun initData() {
        scroller = Scroller(context)

        lrcHeight = ((textSize + dividerHeight) * rows + 5).toInt()

        //初始化画笔
        normalPaint = Paint()
        currentPaint = Paint()

        normalPaint.textSize = textSize
        normalPaint.color = Color.WHITE
        normalPaint.isAntiAlias = true
        currentPaint.textSize = textSize
        currentPaint.color = Color.GREEN
        currentPaint.isAntiAlias = true

        currentPaint.getTextBounds(DEFAUKT_TEXT, 0, DEFAUKT_TEXT.length, textBounds)
        maxScroll = (textBounds.height() + dividerHeight).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec) //?
        //重新设置View的高度
        val measureHeightSpec = MeasureSpec.makeMeasureSpec(lrcHeight, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, measureHeightSpec)
    }

    /**
     * 设置歌词.
     * */
    fun setLyrics(lyricText: String) {
        lryList.addAll(parseLyrics(lyricText))
    }

    /**
     * 解析歌词的文本.
     * */
    private fun parseLyrics(lyricText: String): MutableList<Lyrics> {
        val lryList = mutableListOf<Lyrics>()
        val lyricsArray: List<String> = lyricText.split("\n")
        for (i in 0..lyricsArray.size - 1) {
            //每一行文本，包括 歌词 与 时间
            val text = lyricsArray[i]
            //歌词
            val lyric = text.substring(text.indexOf("]") + 1)
            //若像 [xxx]，后面没有东西，直接丢弃.
            if (lyric.equals("")) {
                continue
            }
            //开始时的时间
            val time = text.substring(text.indexOf("["), text.indexOf("]") + 1)
            if (time.contains(".")) {
                val min = time.substring(time.indexOf("[") + 1, time.indexOf("[") + 3)
                val second = time.substring(time.indexOf(":") + 1, time.indexOf(":") + 3)
                val mills = time.substring(time.indexOf(".") + 1, time.indexOf("]"))
                val startTime = min.toLong() * 60 * 1000 + second.toLong() * 1000 + mills.toLong()
                lryList.add(Lyrics(lyric, startTime))
            }
        }
        return lryList
    }

    override fun onDraw(canvas: Canvas?) {
        //得到中心的Y坐标
        val centerY = (measuredHeight + textBounds.height()) / 2 //?
        //如果歌词列表是空的，那么就提示无歌词
        if(lryList.isEmpty()){
            canvas?.drawText(DEFAUKT_TEXT,viewWidth - currentPaint.measureText(DEFAUKT_TEXT),
                centerY.toFloat(),currentPaint)
        }
        
        //要高亮显示的歌词文本
        val currentLrc = lryList[currentLine].text
        //要高亮显示的歌词文本的X坐标
        val currentX = (viewWidth - currentPaint.measureText(currentLrc)) / 2
        //画当前行
        canvas?.drawText(currentLrc?:"",currentX,centerY - offSetY,currentPaint)
        
        val span = textBounds.height() + dividerHeight //?
        //要显示的第一行的下标
        var firstLine = currentLine - rows/2
        //边界检查
        firstLine = if(firstLine < 0) 0 else firstLine
        var lastLine = currentLine + rows/2 + 2 //?
        //边界检查
        lastLine = if(lastLine > lryList.size - 1) lryList.size else lastLine

        var j = 1
        //画中间行上面的歌词
        for(i in currentLine-1..firstLine){
            //拿到歌词
            val lrcText = lryList[i].text
            val x = (viewWidth - normalPaint.measureText(lrcText)) / 2
            //绘制歌词
            canvas?.drawText(lrcText?:"",x,centerY - j * span - offSetY,normalPaint)
            j++
        }
        
        j = 1
        for(i in currentLine+1..lastLine){
            //拿到歌词
            val lycText = lryList[i].text
            
        }
        
    }

    fun reset() {
        lryList.clear()
        currentLine = 0
        nextTime = 0
        offSetY = 0F
        postInvalidate()//?
    }

    /**
     * 判断是否有歌词数据.
     * */
    fun hasLrc(): Boolean {
        return !lryList.isEmpty()
    }
}