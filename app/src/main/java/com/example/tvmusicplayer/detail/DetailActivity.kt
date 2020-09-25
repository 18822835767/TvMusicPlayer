package com.example.tvmusicplayer.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.service.PlayServiceManager
import com.example.tvmusicplayer.service.SimplePlayObserver
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PAUSE
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PLAY
import com.example.tvmusicplayer.util.ThreadUtil
import com.example.tvmusicplayer.widget.RotationCircleImage
import com.squareup.picasso.Picasso

/**
 * 歌曲播放的详情页.
 * */
class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "DetailActivity"
    private lateinit var backIv: ImageView
    private lateinit var songNameTv: TextView
    private lateinit var singerNameTv: TextView
    private lateinit var coverIv: RotationCircleImage
    private lateinit var endTimeTv: TextView
    private lateinit var playModeIv: ImageView
    private lateinit var preOneIv: ImageView
    private lateinit var playOrPauseIv: ImageView
    private lateinit var nextOneIv: ImageView
    private lateinit var queueIv: ImageView
    private lateinit var seekBar: SeekBar

    /**
     * 判断用户是否触碰了进度条.
     * */
    private var userTouchProgress: Boolean = false

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, DetailActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var observer: SimplePlayObserver = object : SimplePlayObserver() {
        override fun onPlayStateChange(playState: Int) {
            ThreadUtil.runOnUi(Runnable {
                when (playState) {
                    PLAY_STATE_PLAY -> {
                        playOrPauseIv.setImageResource(R.drawable.ic_pause_white)
//                        coverIv.setRotation(true)
                    }
                    PLAY_STATE_PAUSE -> {
                        playOrPauseIv.setImageResource(R.drawable.ic_play_white)
//                        coverIv.setRotation(false)
                    }
                }
            })
        }

        override fun onSeekChange(currentPosition: Int) {
            if (!userTouchProgress) {
                ThreadUtil.runOnUi(Runnable { seekBar.progress = currentPosition })
            }
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
        setContentView(R.layout.activity_deatil)

        //活动主体填充状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.statusBarColor = Color.TRANSPARENT
        }

        initView()
        initData()
        initEvent()
    }

    private fun initView() {
        backIv = findViewById(R.id.ic_back)
        songNameTv = findViewById(R.id.song_name_tv)
        singerNameTv = findViewById(R.id.singer_name_tv)
        coverIv = findViewById(R.id.cover_iv)
        endTimeTv = findViewById(R.id.end_time_tv)
        playModeIv = findViewById(R.id.play_mode)
        preOneIv = findViewById(R.id.pre_one)
        playOrPauseIv = findViewById(R.id.play_or_pause)
        nextOneIv = findViewById(R.id.next_one)
        queueIv = findViewById(R.id.play_queue)
        seekBar = findViewById(R.id.seek_bar)
    }

    private fun initData() {
        //注册观察者
        PlayServiceManager.registerObserver(observer)
    }

    override fun onResume() {
        super.onResume()
        resumeUIInfo()
    }

    private fun initEvent() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //进度条发生改变时
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                userTouchProgress = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    val touchProgress: Int = it.progress
                    PlayServiceManager.seekTo(touchProgress)
                }
                userTouchProgress = false
            }
        })

        playOrPauseIv.setOnClickListener(this)
        nextOneIv.setOnClickListener(this)
        preOneIv.setOnClickListener(this)

    }

    override fun onDestroy() {
        PlayServiceManager.unregisterObserver(observer)
        super.onDestroy()
    }

    /**
     * 设置播放栏的UI信息.
     * */
    private fun resumeUIInfo() {
        //设置播放状态的按钮.
        when (PlayServiceManager.getPlayState()) {
            PLAY_STATE_PLAY -> playOrPauseIv.setImageResource(R.drawable.ic_pause_white)
            PLAY_STATE_PAUSE -> playOrPauseIv.setImageResource(R.drawable.ic_play_white)
        }
        
        PlayServiceManager.getCurrentSong()?.let { 
            setSongInfo(it)
        }
    }
    
    private fun setSongInfo(song : Song){
        songNameTv.text = song.name
        singerNameTv.text = song.artistName
        Picasso.get().load(song.picUrl)
            .resize(250,250)
            .placeholder(R.drawable.album_default_view)
            .error(R.drawable.load_error)
            .into(coverIv)
    }
    

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.play_or_pause -> PlayServiceManager.playOrPause()
                R.id.next_one -> PlayServiceManager.playNext()
                R.id.pre_one -> PlayServiceManager.playPre()
            }
        }
    }
}
