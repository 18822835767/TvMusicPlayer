package com.example.tvmusicplayer.bottomPlayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.detail.DetailActivity
import com.example.tvmusicplayer.service.PlayServiceManager
import com.example.tvmusicplayer.service.SimplePlayObserver
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PAUSE
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PLAY
import com.example.tvmusicplayer.util.ThreadUtil

class BottomPlayerFragment : Fragment() {

    private lateinit var bottomRl: RelativeLayout
    private lateinit var playOrPauseIv : ImageView
    

    companion object {
        @JvmStatic
        fun newInstance(): BottomPlayerFragment {
            return BottomPlayerFragment()
        }
    }

    private var observer : SimplePlayObserver = object : SimplePlayObserver(){
        override fun onPlayStateChange(playState: Int) {
            ThreadUtil.runOnUi(Runnable {
                when (playState) {
                    PLAY_STATE_PLAY -> {
                       playOrPauseIv.setImageResource(R.drawable.ic_black_pause)
                    }
                    PLAY_STATE_PAUSE -> {
                        playOrPauseIv.setImageResource(R.drawable.ic_black_play)
                    }
                }
            })
        }

        override fun onSongChange(song: Song?) {
            
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        //注册观察者
        PlayServiceManager.registerObserver(observer)
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
        playOrPauseIv = view.findViewById(R.id.play_or_pause_iv)
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

    override fun onDestroy() {
        //取消注册
        PlayServiceManager.unregisterObserver(observer)
        
        super.onDestroy()
    }
}
