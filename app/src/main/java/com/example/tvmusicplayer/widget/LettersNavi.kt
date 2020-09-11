package com.example.tvmusicplayer.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class LettersNavi : View {
    private val textPaint = Paint()
    private val s = arrayOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
        "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z", "#"
    )

    /**
     * 鼠标点击时，滑动时选择的字母的下标位置.
     */
    private var choose = -1

    /**
     * 中间显示的字母.
     */
    private var textView: TextView? = null
    private var listener: OnTouchLetterListener? = null

    constructor(context: Context?) : super(context) {}
   
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : 
            super(context, attrs, defStyleAttr) {
    }

    private fun initPaint() {
        textPaint.textSize = 20f
        textPaint.isAntiAlias = true //开启锯齿
        textPaint.color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画字母
        drawText(canvas)
    }

    /**
     * 画字母.
     */
    private fun drawText(canvas: Canvas) {
        //View的宽高
        val width = width
        val height = height

        //获取每个字母的高度
        val singleHeight = height / s.size

        //遍历每个字母，draw
        for (i in s.indices) {
            initPaint()
            if (choose == i) {
                textPaint.color = Color.RED
            }
            //计算每个字母的坐标
            val x = (width - textPaint.measureText(s[i])) / 2
            val y = (i + 1) * singleHeight.toFloat()
            canvas.drawText(s[i], x, y, textPaint)
            //重置画笔颜色
            textPaint.reset()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        //计算选中的字母的下标
        var index = (event.y / height * s.size).toInt()
        //防止脚标越界
        if (index >= s.size) {
            index = s.size - 1
        } else if (index < 0) {
            index = 0
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                setBackgroundColor(Color.GRAY)
                //选中字母高亮
                choose = index
                //出现中间文字
                textView!!.visibility = VISIBLE
                textView!!.text = s[choose]
                //调用接口
                if (listener != null) {
                    listener!!.touchLetterListener(s[choose])
                }
                //重绘
                invalidate()
            }
            else -> {
                setBackgroundColor(Color.TRANSPARENT)
                //取消选中字母高量
                choose = -1
                //隐藏中间文字
                textView!!.visibility = GONE
                //重绘
                invalidate()
            }
        }
        return true
    }

    fun setTextView(textView: TextView?) {
        this.textView = textView
    }

    fun setListener(listener: OnTouchLetterListener?) {
        this.listener = listener
    }

    interface OnTouchLetterListener {
        fun touchLetterListener(s: String?)
    }
}
