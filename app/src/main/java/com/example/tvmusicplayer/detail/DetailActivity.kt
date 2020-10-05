package com.example.tvmusicplayer.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.adapter.PlayQueueAdapter
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.service.PlayServiceManager
import com.example.tvmusicplayer.service.SimplePlayObserver
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.LOOP_PLAY
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.NULL_INT_FLAG
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
class DetailActivity : AppCompatActivity(), View.OnClickListener, DetailContract.OnView,
    LrcView.OnSeekListener, PlayQueueAdapter.OnRemoveClickListener,
    BaseRecyclerViewAdapter.OnItemClickListener {

    private val TAG = "DetailActivity"
    private lateinit var backIv: ImageView
    private lateinit var songNameTv: TextView
    private lateinit var singerNameTv: TextView
    private lateinit var coverIv: RotationCircleImage
    private lateinit var endTimeTv: TextView
    private lateinit var nowPointTv: TextView
    private lateinit var playModeIv: ImageView
    private lateinit var preOneIv: ImageView
    private lateinit var playOrPauseIv: ImageView
    private lateinit var nextOneIv: ImageView
    private lateinit var queueIv: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var lrcView: LrcView
    private lateinit var popupWindow: PopupWindow

    private lateinit var presenter: DetailContract.Presenter
    private lateinit var queueRv: RecyclerView
    private lateinit var queueView: View
    private lateinit var queueAdapter: PlayQueueAdapter
    private lateinit var queueManager: LinearLayoutManager

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

            lrcView.onProgress(currentPosition.toLong())
        }

        override fun onSongChange(song: Song?,position : Int) {
            ThreadUtil.runOnUi(Runnable {
                lrcView.clearLyrics()
                song?.let {
                    setSongInfo(it)
                }
            })
        }

        override fun onSongsEmpty() {
            ThreadUtil.runOnUi(Runnable { resetInfo() })
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

        setUIInfo()
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

        queueView = View.inflate(this, R.layout.play_queue, null)
        queueRv = queueView.findViewById(R.id.queue_rv)
    }

    private fun initData() {
        presenter = DetailPresenter(this)
        lrcView.seekListener = this

        //设置队列的recyclerView的数据
        queueAdapter = PlayQueueAdapter(mutableListOf<Song>(), R.layout.play_queue_item)
        queueAdapter.setOnRemoveClickListener(this)
        queueAdapter.setItemClickListener(this)
        queueManager = LinearLayoutManager(this)
        queueRv.adapter = queueAdapter
        queueRv.layoutManager = queueManager
        queueRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        initPopupWindow()

        //注册观察者
        PlayServiceManager.registerObserver(observer)
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
                    lrcView.onDrag(it.progress.toLong())
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
        queueIv.setOnClickListener(this)
    }

    override fun onDestroy() {
        PlayServiceManager.unregisterObserver(observer)
        super.onDestroy()
    }

    /**
     * 设置播放栏的UI信息.
     * */
    private fun setUIInfo() {
        //设置播放状态的按钮.
        when (PlayServiceManager.getPlayState()) {
            PLAY_STATE_PLAY -> playOrPauseIv.setImageResource(R.drawable.ic_pause_white)
            PLAY_STATE_PAUSE -> playOrPauseIv.setImageResource(R.drawable.ic_play_white)
        }

        when (PlayServiceManager.getPlayMode()) {
            ORDER_PLAY -> playModeIv.setImageResource(R.drawable.ic_order_play)
            RANDOM_PLAY -> playModeIv.setImageResource(R.drawable.ic_random_play)
            LOOP_PLAY -> playModeIv.setImageResource(R.drawable.ic_loop_play)
        }

        PlayServiceManager.getCurrentSong()?.let {
            setSongInfo(it)
        }

        if (PlayServiceManager.getPlayState() != PLAY_STATE_PLAY) {
            coverIv.setRotation(false)
        }
    }

    private fun setSongInfo(song: Song) {
        songNameTv.text = song.name
        singerNameTv.text = song.artistName
        val duration = PlayServiceManager.getDuration()
        seekBar.max = duration
        seekBar.progress = PlayServiceManager.getCurrentPoint()
        endTimeTv.text = TextUtil.getTimeStr(duration.toLong())
        //当歌曲是线上歌曲时，才去获取歌词，加载网络图片
        if (song.online) {
            song.id?.let {
                presenter.getSongLyrics(it)
            }

            Picasso.get().load(song.picUrl)
                .resize(250, 250)
                .placeholder(R.drawable.album_default_view)
                .error(R.drawable.album_default_view)
                .into(coverIv)
            //本地歌曲时，只加载资源图片
        } else {
            Picasso.get().load(R.drawable.album_default_view)
                .resize(250, 250)
                .into(coverIv)
        }
    }

    /**
     * 重置信息，除了播放模式之外.
     * */
    private fun resetInfo() {
        songNameTv.text = "歌曲名字"
        singerNameTv.text = "歌手"
        seekBar.progress = 0
        seekBar.max = 0
        endTimeTv.text = "00:00"
        lrcView.reset()
        playOrPauseIv.setImageResource(R.drawable.ic_play_white)
        Picasso.get().load(R.drawable.album_default_view)
            .resize(250, 250)
            .into(coverIv)
    }

    private fun initPopupWindow() {
        popupWindow = PopupWindow(queueView)
        //设置窗口大小
        popupWindow.width = WindowManager.LayoutParams.MATCH_PARENT
        popupWindow.height = 1000
        //设置下面两项，使得弹出窗口可以响应back等物理时间
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(0x00000000))
    }

    private fun showPopupWindow() {
        //设置弹出窗口的位置.
        popupWindow.showAtLocation(queueIv, Gravity.BOTTOM, 0, 0)

        val curPos = PlayServiceManager.getCurrentPosition()
        if(curPos != NULL_INT_FLAG){
            queueAdapter.curPosition = curPos
        }
        
        PlayServiceManager.getQueueSongs()?.let {
            queueAdapter.clearAndAddNewDatas(it)
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.play_or_pause -> PlayServiceManager.playOrPause()
                R.id.next_one -> PlayServiceManager.playNext()
                R.id.pre_one -> PlayServiceManager.playPre()
                R.id.play_mode -> {
                    when (PlayServiceManager.getPlayMode()) {
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
                R.id.play_queue -> {
                    showPopupWindow()
                }
            }
        }
    }

    private fun showText(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun getLyricsSuccess(lyricsText: String) {
        lrcView.reset()
        lrcView.setLyrics(lyricsText)
    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onSeek(time: Long) {
        PlayServiceManager.seekTo(time.toInt())
    }

    override fun onRemoveClick(v: View?, position: Int) {
//        Toast.makeText(this,"点击位置：${position}",Toast.LENGTH_SHORT).show()
        queueAdapter.removeItem(position)
        ThreadUtil.runOnThreadPool(Runnable { PlayServiceManager.removeSong(position) })
    }

    override fun onItemClick(v: View?, position: Int) {
        ThreadUtil.runOnThreadPool(Runnable { PlayServiceManager.playSongByIndex(position)})
    }
}
