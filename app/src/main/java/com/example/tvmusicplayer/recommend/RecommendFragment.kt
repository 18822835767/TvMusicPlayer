package com.example.tvmusicplayer.recommend

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.adapter.CircleButtonAdapter
import com.example.tvmusicplayer.widget.BannerViewPager

class RecommendFragment : Fragment() {
    
    private lateinit var banner : BannerViewPager 
    private lateinit var circleButtonRv : RecyclerView
    
    private var circleButtonText = mutableListOf<String>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recommend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        banner = view.findViewById(R.id.banner_view_pager)
        circleButtonRv = view.findViewById(R.id.circle_button_rv)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        //测试代码
        val drawableList = mutableListOf<Drawable?>()
        drawableList.add(context?.getDrawable(R.drawable.test))
        drawableList.add(context?.getDrawable(R.drawable.test))
        drawableList.add(context?.getDrawable(R.drawable.test))
        drawableList.add(context?.getDrawable(R.drawable.test))
        banner.setData(drawableList)
        
//        //推荐面的一排圆形按钮
//        circleButtonText.add("私人FM")
//        circleButtonText.add("歌单")
//        circleButtonText.add("排行榜")
//        circleButtonText.add("直播")
//        circleButtonText.add("电台")
//        circleButtonText.add("数字专辑")
//        val circleButtonAdapter = CircleButtonAdapter(circleButtonText,R.layout.circle_button_item)
//        val manager = LinearLayoutManager(context)
//        manager.orientation = LinearLayoutManager.HORIZONTAL
//        circleButtonRv.adapter = circleButtonAdapter
//        circleButtonRv.layoutManager = manager
    }

    companion object {
        @JvmStatic
        fun newInstance(): RecommendFragment {
            return RecommendFragment()
        }
    }
}
