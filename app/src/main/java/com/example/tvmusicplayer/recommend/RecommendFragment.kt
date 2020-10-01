package com.example.tvmusicplayer.recommend

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.widget.BannerViewPager

class RecommendFragment : Fragment() {
    
    private lateinit var banner : BannerViewPager 
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recommend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        banner = view.findViewById(R.id.banner_view_pager)
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
    }

    companion object {
        @JvmStatic
        fun newInstance(): RecommendFragment {
            return RecommendFragment()
        }
    }
}
