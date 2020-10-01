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

    private lateinit var scroller: Scroller

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

    //?
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = measuredWidth
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
        val lyricsArray: List<String> = lyricText.split("\\n") //?待定。第一个斜杆用于转义
        for (element in lyricsArray) {
            //每一行文本，包括 歌词 与 时间
            val text = element
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
        if (lryList.isEmpty()) {
            canvas?.drawText(
                DEFAUKT_TEXT, viewWidth - currentPaint.measureText(DEFAUKT_TEXT),
                centerY.toFloat(), normalPaint
            )
            return
        }

        //要高亮显示的歌词文本
        val currentLrc = lryList[currentLine].text
        //要高亮显示的歌词文本的X坐标
        val currentX = (viewWidth - currentPaint.measureText(currentLrc)) / 2
        //画当前行
        canvas?.drawText(currentLrc, currentX, centerY - offSetY, currentPaint)

        val span = textBounds.height() + dividerHeight //?
        //要显示的第一行的下标
        var firstLine = currentLine - rows / 2
        //边界检查
        firstLine = if (firstLine < 0) 0 else firstLine
        var lastLine = currentLine + rows / 2 + 2 //?
        //边界检查
        lastLine = if (lastLine > lryList.size - 1) lryList.size else lastLine

        var j = 1
        if (currentLine != 0) {
            //画中间行上面的歌词
            for (i in currentLine - 1 downTo firstLine) {
                //拿到歌词
                val lrcText = lryList[i].text
                val x = (viewWidth - normalPaint.measureText(lrcText)) / 2
                //绘制歌词
                canvas?.drawText(lrcText, x, centerY - j * span - offSetY, normalPaint)
                j++
            }
        }

        j = 1
        //画中间行下面的歌词
        for (i in currentLine + 1..lastLine) {
            if(i < lryList.size){
                //拿到歌词
                val lycText = lryList[i].text
                val x = (viewWidth - normalPaint.measureText(lycText)) / 2
                canvas?.drawText(lycText, x, centerY + j * span - offSetY, normalPaint)
                j++
            }
        }
    }

    /**
     * 音乐播放器的回调.
     * 为什么加锁?
     * */
    @Synchronized fun onProgress(time : Long) {
        //如果当前时间小于下一句开始的时间,直接return
        if(nextTime > time){
            return
        }
        
        val size = lryList.size
        for(i in 0 until size){
            //解决最后一行不能高亮显示的问题
            if (nextTime == lryList[size - 1].start) {
                currentLine = size - 1
                nextTime = nextTime + 60 * 1000 //让nextTime变大，使其不会被重复绘制
                scroller.abortAnimation()
                scroller.startScroll(size, 0, 0, maxScroll, SCROLL_TIME)
                postInvalidate() //重绘
                break
            }
            
            //找到大于当前时间的，作为下一行
            if(lryList[i].start > time){
                nextTime = lryList[i].start
                //若 时间<第一句歌词开始时间 或者 在第一句歌词时间范围内，那么直接第一句歌词高亮.
                if(i == 0 || i == 1){
                    postInvalidate()
                    break
                }
                currentLine = i - 1

                scroller.abortAnimation()//? 若有未完成的滚动，完成它，终止
                //这里的滚动，其实是之前高亮的那一行，要先缓慢地往上滚动，使得歌词的切换有个过渡的效果
                //在computeScroll()中，会等之前高亮的那一行往上滚动结束后，将当前的行高亮
                scroller.startScroll(i,0,0,maxScroll, SCROLL_TIME)//这里的i用于记录下一行的行数
                postInvalidate()
                break
            }
        }
    }

    override fun computeScroll() {
        if(scroller.computeScrollOffset()){
            //根据Scroller的计算，获取滚动的过程中，Y方向上应该有的偏移量
            offSetY = scroller.currY.toFloat()
//            //如果滚动已经结束
//            if(scroller.isFinished){
//                //获取下一行
//                val nextLine = scroller.currX
//                //转化为当前应该显示的行
//                currentLine = if(nextLine <= 1) 0 else nextLine - 1
//                //显示当前应该显示的行
//            }
            postInvalidate()
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