package com.example.tvmusicplayer.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.tvmusicplayer.R
import com.squareup.picasso.Picasso
import java.lang.ref.WeakReference
import java.util.*

/**
 * 实现轮播图，自定义控件.
 *
 *
 * 通过无线循环实现。
 * 假如展示三张图片V1 V2 V3，那么程序中的图片实际上是V3 V1 V2 V3 V1.
 *
 */
class BannerViewPager(private val mContext: Context, attrs: AttributeSet?) : FrameLayout(mContext, attrs) {
    private var mViewPager: ViewPager? = null

    companion object{
        private val START = 10
        private val STOP = 20
    }

    init {
        init(mContext, attrs)
    }
    
    /**
     * 展示 小圆点的线性布局
     */
    private var mIndicatorGroup: LinearLayout? = null
    
    /**
     * 存放"实际"要展示的图片的url.
     */
    private val imageUrls : MutableList<String?> = mutableListOf()

    /**
     * 存放"实际"要展示的图片的ImageView.
     */
    private val mViews: MutableList<View> = mutableListOf()

    /**
     * 存放 小圆点的数组
     */
    private lateinit var mTips: Array<ImageView?>

    /**
     * 用户"看到"的图片数量
     */
    private var mCount = 0

    /**
     * 轮播图的间隔时间，即1.5s.
     */
    private val mBannerTime = 1500

    /**
     * 表示 轮播图的当前选中项(从0开始).
     */
    private var mCurrentItem = 0

    /**
     * 保存 手 滑动时的时间。下面进行判断，防止手滑动后又立即轮播
     */
    private var mSlideTime: Long = 0
    private var mHandler: Handler? = null

    /**
     * 加载“轮播图”布局、初始化控件、handler消息机制.
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.banner_view_pager, this)
        mViewPager = view.findViewById(R.id.view_pager)
        mIndicatorGroup = findViewById(R.id.indicator)
        mHandler = UIHandler(this)
    }

    /**
     * 主要是向handler发送消息，控制轮播图是否立即放下一张图片.
     */
    private val runnable = Runnable {
        val now = System.currentTimeMillis() //记录现在的时间
        if (now - mSlideTime > mBannerTime - 500) {
            //正常情况；或者手指滑动，且手指滑动时间较早
            mHandler!!.sendEmptyMessage(START)
        } else {
            //手指滑动，且滑动时间较晚
            mHandler!!.sendEmptyMessage(STOP)
        }
    }

    /**
     * 初始化imageUrls的资源。
     */
    fun setData(urls: List<String?>) {
        if(urls.isNotEmpty()){
            mViews.clear()
            mCount = urls.size
            imageUrls.add(urls[mCount - 1])
            imageUrls.addAll(urls)
            imageUrls.add(urls[0])
            initIndicator()
            showImage
            setUI()
        }
    }

    /**
     * 设置“小圆点”指示器，并且添加到线性布局中.
     */
    private fun initIndicator() {
        mTips = arrayOfNulls(mCount)
        //设置小圆点在线性布局中的参数
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        layoutParams.height = 20
        layoutParams.width = 20
        layoutParams.leftMargin = 10 //点的左边距
        layoutParams.rightMargin = 10 //点的右边距
        for (i in 0 until mCount) {
            val imageView = ImageView(mContext)
            //先设置第一个小圆点为红色，其他圈为黑色
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.red_circle)
            } else {
                imageView.setBackgroundResource(R.drawable.black_circle)
            }
            mTips[i] = imageView
            mIndicatorGroup!!.addView(imageView, layoutParams)
        }
    }

    /**
     * drawable加载到imageView，并且存放到views容器里
     */
    private val showImage: Unit
        private get() {
            for (i in imageUrls.indices) {
                val imageView = ImageView(mContext)
                Picasso.get().load(imageUrls[i])
                    .into(imageView)
                mViews.add(imageView)
            }
        }

    /**
     * 设置UI.
     *
     *
     * 为ViewPager加适配器，监听器。设置初始的图片，handle消息机制.
     *
     */
    private fun setUI() {
        val adapter = BannerAdapter()
        mViewPager!!.adapter = adapter
        mViewPager!!.addOnPageChangeListener(onPageChangeListener)
        mViewPager!!.currentItem = 1
        mHandler!!.postDelayed(runnable, mBannerTime.toLong())
    }

    /**
     * 监听ViewPager的pager的改变
     */
    private val onPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        //根据当前的图片位置计算对应的小圆点的下标
        override fun onPageSelected(position: Int) {
            val max = mViews.size
            mCurrentItem = position
            if (position == 0) {
                //选择到最左边的pager时
                mCurrentItem = max - 2
            } else if (position == max - 1) {
                //选择到最右边的pager时
                mCurrentItem = 1
            }
            //小圆点的下标
            val temp: Int = mCurrentItem - 1 //当前pager的下标
            setIndicator(temp)
        }

        override fun onPageScrollStateChanged(state: Int) {
            mCurrentItem = mViewPager!!.currentItem
            when (state) {
                ViewPager.SCROLL_STATE_IDLE -> if (mCurrentItem == 0) {
                    //滑动到最左边的那张时,重新设置图片
                    mViewPager!!.setCurrentItem(mCount, false)
                } else if (mCurrentItem == mCount + 1) {
                    //滑动到最右边的那张时,重新设置图片
                    mViewPager!!.setCurrentItem(1, false)
                }
                ViewPager.SCROLL_STATE_DRAGGING ->                     //记录用户用手开始拖拽时的时间
                    mSlideTime = System.currentTimeMillis()
                else -> {
                }
            }
        }
    }

    /**
     * 小圆点颜色的切换.
     *
     *
     * 把当前正显示的图片的小圆点变红色.
     *
     */
    private fun setIndicator(position: Int) {
        for (i in mTips.indices) {
            if (i == position) {
                mTips[i]!!.setBackgroundResource(R.drawable.red_circle)
            } else {
                mTips[i]!!.setBackgroundResource(R.drawable.black_circle)
            }
        }
    }

    private class UIHandler internal constructor(bannerViewPager: BannerViewPager) :
        Handler() {
        var mBannerWeakRef: WeakReference<BannerViewPager> = WeakReference(bannerViewPager)
        override fun handleMessage(msg: Message) {
            val banner = mBannerWeakRef.get()
            if (banner != null) {
                when (msg.what) {
                    START -> {
                        banner.mViewPager!!.currentItem = banner.mCurrentItem + 1
                        banner.mHandler!!.removeCallbacks(banner.runnable)
                        //重新设定任务
                        banner.mHandler!!.postDelayed(banner.runnable, banner.mBannerTime.toLong())
                    }
                    STOP -> {
                        banner.mSlideTime = 0 //重置手滑动的时间
                        banner.mHandler!!.removeCallbacks(banner.runnable)
                        //重新设定任务
                        banner.mHandler!!.postDelayed(banner.runnable, banner.mBannerTime.toLong())
                    }
                    else -> {
                    }
                }
            }
        }

    }

    /**
     * ViewPager适配器.
     */
    internal inner class BannerAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return mViews.size
        }

        override fun isViewFromObject(
            view: View,
            `object`: Any
        ): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(mViews[position])
            return mViews[position]
        }

        override fun destroyItem(
            container: ViewGroup,
            position: Int,
            `object`: Any
        ) {
            container.removeView(`object` as View)
        }
    }
}
