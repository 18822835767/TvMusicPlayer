package com.example.tvmusicplayer.bottomPlayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.detail.DetailActivity

class BottomPlayerFragment : Fragment() {

    private lateinit var bottomRl: RelativeLayout

    companion object {
        @JvmStatic
        fun newInstance(): BottomPlayerFragment {
            return BottomPlayerFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomRl = view.findViewById(R.id.bottom_rl)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //点击底部播放碎片，会跳转到音乐播放的详情页.
        bottomRl.setOnClickListener {
            context?.let {
                DetailActivity.actionStart(it)
            }
        }
    }
}
