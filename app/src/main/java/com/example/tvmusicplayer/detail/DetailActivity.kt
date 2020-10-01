package com.example.tvmusicplayer.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.service.PlayServiceManager
import com.example.tvmusicplayer.service.SimplePlayObserver
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.LOOP_PLAY
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.ORDER_PLAY
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PAUSE
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PLAY
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.RANDOM_PLAY
import com.example.tvmusicplayer.util.LogUtil
import com.example.tvmusicplayer.util.TextUtil
import com.example.tvmusicplayer.util.ThreadUtil
import com.example.tvmusicplayer.widget.LrcView
import com.example.tvmusicplayer.widget.RotationCircleImage
import com.squareup.picasso.Picasso

/**
 * 歌曲播放的详情页.
 * */
class DetailActivity : AppCompatActivity(), View.OnClickListener,DetailContract.OnView{

    private val TAG = "DetailActivity"
    private lateinit var backIv: ImageView
    private lateinit var songNameTv: TextView
    private lateinit var singerNameTv: TextView
    private lateinit var coverIv: RotationCircleImage
    private lateinit var endTimeTv: TextView
    private lateinit var nowPointTv : TextView
    private lateinit var playModeIv: ImageView
    private lateinit var preOneIv: ImageView
    private lateinit var playOrPauseIv: ImageView
    private lateinit var nextOneIv: ImageView
    private lateinit var queueIv: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var lrcView: LrcView
    
    private lateinit var presenter : DetailContract.Presenter

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
                        coverIv.setRotation(true)
                    }
                    PLAY_STATE_PAUSE -> {
                        playOrPauseIv.setImageResource(R.drawable.ic_play_white)
                        coverIv.setRotation(false)
                    }
                }
            })
        }

        override fun onSeekChange(currentPosition: Int) {
            if (!userTouchProgress) {
                ThreadUtil.runOnUi(Runnable { 
                    seekBar.progress = currentPosition
                })
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
        nowPointTv = findViewById(R.id.now_point_tv)
        playModeIv = findViewById(R.id.play_mode)
        preOneIv = findViewById(R.id.pre_one)
        playOrPauseIv = findViewById(R.id.play_or_pause)
        nextOneIv = findViewById(R.id.next_one)
        queueIv = findViewById(R.id.play_queue)
        seekBar = findViewById(R.id.seek_bar)
        lrcView = findViewById(R.id.lrc_view)
    }

    private fun initData() {
        presenter = DetailPresenter(this)
        
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
                nowPointTv.text = TextUtil.getTimeStr(progress.toLong())
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
        playModeIv.setOnClickListener(this)
        backIv.setOnClickListener(this)
        coverIv.setOnClickListener(this)
        lrcView.setOnClickListener(this)
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
        
        when(PlayServiceManager.getPlayMode()){
            ORDER_PLAY -> playModeIv.setImageResource(R.drawable.ic_order_play)
            RANDOM_PLAY -> playModeIv.setImageResource(R.drawable.ic_random_play)
            LOOP_PLAY -> playModeIv.setImageResource(R.drawable.ic_loop_play)
        }
        
        PlayServiceManager.getCurrentSong()?.let { 
            setSongInfo(it)
        }

        if(PlayServiceManager.getPlayState() != PLAY_STATE_PLAY){
            coverIv.setRotation(false)
        }
    }
    
    private fun setSongInfo(song : Song){
        songNameTv.text = song.name
        singerNameTv.text = song.artistName
        val duration = PlayServiceManager.getDuration()
        seekBar.max = duration
        seekBar.progress = PlayServiceManager.getCurrentPoint()
        endTimeTv.text = TextUtil.getTimeStr(duration.toLong())
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
                R.id.play_mode -> {
                    when(PlayServiceManager.getPlayMode()){
                        ORDER_PLAY -> {
                            playModeIv.setImageResource(R.drawable.ic_random_play)
                            showText("随机播放")
                            PlayServiceManager.setPlayMode(RANDOM_PLAY)
                        }
                        RANDOM_PLAY -> {
                            playModeIv.setImageResource(R.drawable.ic_loop_play)
                            showText("单曲循环")
                            PlayServiceManager.setPlayMode(LOOP_PLAY)
                        }
                        LOOP_PLAY -> {
                            playModeIv.setImageResource(R.drawable.ic_order_play)
                            showText("列表播放")
                            PlayServiceManager.setPlayMode(ORDER_PLAY)
                        }
                    }
                }
                R.id.ic_back -> finish()
                R.id.cover_iv -> {
                    coverIv.visibility = View.GONE
                    lrcView.visibility = View.VISIBLE
                }
                R.id.lrc_view -> {
                    lrcView.visibility = View.GONE
                    coverIv.visibility = View.VISIBLE
                }
            }
        }
    }
    
    private fun showText(msg : String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

    override fun getLyricsSuccess(lyricsText: String) {
        lrcView.setLyrics(lyricsText)
    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    override fun showError(errorMessage: String) {
        TODO("Not yet implemented")
    }
}
