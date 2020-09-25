package com.example.tvmusicplayer.bottomPlayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.detail.DetailActivity
import com.example.tvmusicplayer.service.PlayServiceManager
import com.example.tvmusicplayer.service.SimplePlayObserver
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PAUSE
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PLAY
import com.example.tvmusicplayer.util.ThreadUtil
import com.squareup.picasso.Picasso

class BottomPlayerFragment : Fragment() {

    private lateinit var bottomRl: RelativeLayout
    private lateinit var playOrPauseIv: ImageView
    private lateinit var musicCovIv: ImageView
    private lateinit var songNameTv: TextView
    private lateinit var singerNameTv: TextView


    companion object {
        @JvmStatic
        fun newInstance(): BottomPlayerFragment {
            return BottomPlayerFragment()
        }
    }

    private var observer: SimplePlayObserver = object : SimplePlayObserver() {
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
            song?.let {
                ThreadUtil.runOnUi(Runnable {
                    setSongInfo(it)
                })
            }
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
        musicCovIv = view.findViewById(R.id.music_picture)
        songNameTv = view.findViewById(R.id.song_name_tv)
        singerNameTv = view.findViewById(R.id.singer_name_tv)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //点击底部播放碎片，会跳转到音乐播放的详情页.
        bottomRl.setOnClickListener {
            context?.let {
                DetailActivity.actionStart(it)
            }
        }

        //播放或者暂停按钮被点击时
        playOrPauseIv.setOnClickListener {
            PlayServiceManager.playOrPause()
        }
    }

    override fun onResume() {
        super.onResume()
        resumeUIInfo()
    }

    private fun resumeUIInfo() {
        //设置播放状态的按钮.
        when (PlayServiceManager.getPlayState()) {
            PLAY_STATE_PLAY -> playOrPauseIv.setImageResource(R.drawable.ic_black_pause)
            PLAY_STATE_PAUSE -> playOrPauseIv.setImageResource(R.drawable.ic_black_play)
        }

        PlayServiceManager.getCurrentSong()?.let {
            setSongInfo(it)
        }
    }

    private fun setSongInfo(song: Song) {
        songNameTv.text = song.name
        singerNameTv.text = song.artistName
        Picasso.get().load(song.picUrl)
            .resize(250, 250)
            .placeholder(R.drawable.album_default_view)
            .error(R.drawable.load_error)
            .into(musicCovIv)
    }

    override fun onDestroy() {
        //取消注册
        PlayServiceManager.unregisterObserver(observer)

        super.onDestroy()
    }
}
