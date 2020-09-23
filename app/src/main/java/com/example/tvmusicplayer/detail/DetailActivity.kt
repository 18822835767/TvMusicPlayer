package com.example.tvmusicplayer.detail

import android.animation.FloatEvaluator
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tvmusicplayer.R

/**
 * 歌曲播放的详情页.
 * */
class DetailActivity : AppCompatActivity() {
    
    private lateinit var backIv : ImageView
    private lateinit var songNameTv : TextView
    private lateinit var singerNameTv : TextView
    private lateinit var coverIv : ImageView
    private lateinit var endTimeTv : TextView
    private lateinit var playModeIv : ImageView
    private lateinit var preOneIv : ImageView
    private lateinit var playOrPauseIv : ImageView
    private lateinit var nextOneIv : ImageView
    private lateinit var queueIv : ImageView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deatil)
        
        //活动主体填充状态栏
        if(Build.VERSION.SDK_INT >= 21){
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.statusBarColor = Color.TRANSPARENT
        }
        
        initView()
        initData()
    }
    
    private fun initView(){
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
    }
    
    private fun initData(){
        
    }
}
