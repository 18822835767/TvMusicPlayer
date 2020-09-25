package com.example.tvmusicplayer.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import de.hdodenhof.circleimageview.CircleImageView

/**
 * 可以旋转的CircleImageView.
 * */
class RotationCircleImage : CircleImageView {

    private var rotation : Boolean = false
    private lateinit var animator : ObjectAnimator
    
    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initView()
    }
    
    /**
     * 初始化.
     * */
    private fun initView(){
        animator = ObjectAnimator.ofFloat(this,"rotation",0f,360f)
        animator.duration = 20000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.start()
    }
    
    /**
     * 设置旋转.
     * */
    fun setRotation(rotation : Boolean){
        if(this.rotation != rotation){
            this.rotation = rotation
            
            if(rotation){
                animator.resume()
            }else{
                animator.pause()
            }
        }
    }
}