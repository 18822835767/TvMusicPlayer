package com.example.tvmusicplayer.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import com.example.tvmusicplayer.bean.Lyrics
import kotlin.math.abs

/**
 * 显示歌词的控件.
 * */
class LrcView : View {

    private val TAG = "LrcView"

    companion object {
        //滚动时间
        val SCROLL_TIME = 500

        //无歌词时的默认显示
        val DEFAUKT_TEXT = "暂无歌词"
    }

    /**
     * 存放歌词实体类.
     * */
    private val lyrList = mutableListOf<Lyrics>()

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
    private var rows: Int = 12

    /**
     * 记录当前展示、高亮的行(在list中的下标).
     * */
    private var currentLine = -1

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

    private var moving = false

    private var lastY = 0F

    private var lastPosition = 0

    var seekListener: OnSeekListener? = null

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
        lyrList.addAll(parseLyrics(lyricText))
        invalidate()
    }

    /**
     * 解析歌词的文本.
     * */
    private fun parseLyrics(lyricText: String): MutableList<Lyrics> {
        val lryList = mutableListOf<Lyrics>()
//        测试时，歌词直接写在字符串里，字符串中用"\\n"表示"\n"，第一个"\"用于转义；但是这里的歌词直接通过网络获取
//        获取的歌词中是"\n"，无转义字符，所以只用使用"\n"进行分割
//        val lyricsArray: List<String> = lyricText.split("\\n") 
        val lyricsArray: List<String> = lyricText.split("\n")
        for (element in lyricsArray) {
            //每一行文本，包括 歌词 与 时间
            //歌词
            val lyric = element.substring(element.indexOf("]") + 1)
            //若像 [xxx]，后面没有东西，直接丢弃.
            if (lyric.equals("")) {
                continue
            }
            //开始时的时间
            val time = element.substring(element.indexOf("["), element.indexOf("]") + 1)
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
        if (lyrList.isEmpty()) {
            canvas?.drawText(
                DEFAUKT_TEXT, (viewWidth - currentPaint.measureText(DEFAUKT_TEXT)) / 2,
                centerY.toFloat(), normalPaint
            )
            return
        }

        val span = textBounds.height() + dividerHeight //?

        var j = 1
        //如果current等于-1，说明当前还未到第一句歌词的开始时间
        if (currentLine == -1) {
            for (i in 0..rows / 2 + 2) {
                if (i < lyrList.size) {
                    //拿到歌词
                    val lycText = lyrList[i].text
                    val x = (viewWidth - normalPaint.measureText(lycText)) / 2
                    canvas?.drawText(lycText, x, centerY + (j - 1) * span - offSetY, normalPaint)
                    j++
                }
            }
            return
        }

        //要高亮显示的歌词文本
        val currentLrc = lyrList[currentLine].text
        //要高亮显示的歌词文本的X坐标
        val currentX = (viewWidth - currentPaint.measureText(currentLrc)) / 2
        //画当前行
        canvas?.drawText(currentLrc, currentX, centerY - offSetY, currentPaint)

        //要显示的第一行的下标
        var firstLine = currentLine - rows / 2
        //边界检查
        firstLine = if (firstLine < 0) 0 else firstLine
        var lastLine = currentLine + rows / 2 + 2 //?
        //边界检查
        lastLine = if (lastLine > lyrList.size - 1) lyrList.size else lastLine

        j = 1
        if (currentLine != 0) {
            //画中间行上面的歌词
            for (i in currentLine - 1 downTo firstLine) {
                //拿到歌词
                val lrcText = lyrList[i].text
                val x = (viewWidth - normalPaint.measureText(lrcText)) / 2
                //绘制歌词
                canvas?.drawText(lrcText, x, centerY - j * span - offSetY, normalPaint)
                j++
            }
        }

        j = 1
        //画中间行下面的歌词
        for (i in currentLine + 1..lastLine) {
            if (i < lyrList.size) {
                //拿到歌词
                val lycText = lyrList[i].text
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
    @Synchronized
    fun onProgress(time: Long) {
        if (moving) {
            return
        }
        //如果当前时间小于下一句开始的时间,直接return
        if (nextTime > time) {
            return
        }

        val size = lyrList.size
        for (i in 0 until size) {
            //解决最后一行不能高亮显示的问题
            if (nextTime == lyrList[size - 1].start) {
                currentLine = size - 1
                nextTime += Long.MAX_VALUE //让nextTime变大，使歌词不会被重复绘制
                scroller.abortAnimation()
                scroller.startScroll(size, 0, 0, maxScroll, SCROLL_TIME)
                postInvalidate() //重绘
                break
            }

            //找到大于当前时间的，作为下一行
            if (lyrList[i].start > time) {
                nextTime = lyrList[i].start
                //时间 < 第一句歌词开始时间
                if (i == 0) {
                    currentLine = -1
                    postInvalidate()
                    return
                }

                currentLine = i - 1

                scroller.abortAnimation()//? 若有未完成的滚动，完成它，终止
                scroller.startScroll(i, 0, 0, maxScroll, SCROLL_TIME)//这里的i用于记录下一行的行数
                postInvalidate()
                break
            }
        }
    }

    /**
     * 用户在释放拖动的进度条后调用，用于改变歌词的位置.
     * */
    fun onDrag(progress: Long) {
        for (i in lyrList.indices) {
            if (lyrList[i].start > progress) {
                nextTime = if (i == 0) 0 else lyrList[i - 1].start
                return
            }
        }
        //这里主要是处理，直接将进度条拉到快结束的那种状态，如果不这样处理，歌词将不会随着进度条的移动而移动.
        if (lyrList.isNotEmpty()) {
            nextTime = lyrList[lyrList.size - 1].start
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(lyrList.isNotEmpty()){
            event?.let {
                val y = it.y

                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastY = y
                        lastPosition = currentLine
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
                        val tempOffsetY = y - lastY
                        if (abs(tempOffsetY) > touchSlop) {
                            moving = true
                            val lineOffset = tempOffsetY / maxScroll
//                        Log.d(TAG, "$lineOffset")
                            currentLine = lastPosition - lineOffset.toInt()
                            //边界控制
                            if (currentLine < 0) {
                                currentLine = 0
                            } else if (currentLine >= lyrList.size) {
                                currentLine = lyrList.size - 1
                            }
                            invalidate()
                        }
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        if (moving) {
                            seekListener?.let {
                                it.onSeek(lyrList[currentLine].start)

                                if(currentLine == lyrList.size - 1){
                                    nextTime = Long.MAX_VALUE
                                }else if(currentLine >= 0 && currentLine < lyrList.size - 1){
                                    nextTime = lyrList[currentLine+1].start
                                }

                            }
                            moving = false
                            //这里返回false，是因为onTouchEvent()的执行有两种情况：第一种是点击，这时候moving是
                            //false(在ACTION_MOVE中并没有被赋值)，不会进入if体，所以最后调用super.onTouchEven
                            // t(event)，使得点击事件被触发；第二种是滑动歌词，这时候moving是true，进入if体，
                            //返回false，因为onClick()并没有收到ACTION_UP事件，所以onClick()不会被触发
                            return false
                        }
                    }
                }
            }
        }
        //这里调用super...，是为了使得onClick()也有机会被执行.
        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            //根据Scroller的计算，获取滚动的过程中，Y方向上应该有的偏移量
            offSetY = scroller.currY.toFloat()
            postInvalidate()
        }
    }

    fun reset() {
        lyrList.clear()
        currentLine = -1
        nextTime = 0
        offSetY = 0F
        postInvalidate()//?
    }

    /**
     * 判断是否有歌词数据.
     * */
    fun hasLrc(): Boolean {
        return lyrList.isNotEmpty()
    }

    interface OnSeekListener {
        fun onSeek(time: Long)
    }
}