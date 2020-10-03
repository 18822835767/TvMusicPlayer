package com.example.tvmusicplayer.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.widget.ImageView

class CircleImage : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun draw(canvas: Canvas?) {
        canvas?.let {
            val path = Path()
            path.addCircle(
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                (height / 2).toFloat(),
                Path.Direction.CCW
            )
            it.save()
            it.clipPath(path)
            super.draw(canvas)
            it.restore()
        }

        if(canvas == null){
            super.draw(canvas)
        }
    }
}