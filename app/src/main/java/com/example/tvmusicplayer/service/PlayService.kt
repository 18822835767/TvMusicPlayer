package com.example.tvmusicplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.RemoteCallbackList
import android.widget.Toast
import com.example.tvmusicplayer.IPlayInterface
import com.example.tvmusicplayer.IPlayObserver
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.manager.NotifyManager
import com.example.tvmusicplayer.model.DownloadSongModel
import com.example.tvmusicplayer.model.SongInfoModel
import com.example.tvmusicplayer.model.impl.DownloadSongModelImpl
import com.example.tvmusicplayer.model.impl.SongInfoModelImpl
import com.example.tvmusicplayer.network.DownloadUtil
import com.example.tvmusicplayer.network.SimpleDownloadListener
import com.example.tvmusicplayer.util.Constant
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.LOOP_PLAY
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.NULL_URL
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.ORDER_PLAY
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PAUSE
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_PLAY
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.PLAY_STATE_STOP
import com.example.tvmusicplayer.util.Constant.PlaySongConstant.RANDOM_PLAY
import com.example.tvmusicplayer.util.LogUtil
import com.example.tvmusicplayer.util.ThreadUtil
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class PlayService : Service() {

    private val TAG = "PlayService"
    private var mediaPlayer: MediaPlayer? = null
    private var timer: Timer? = null
    private var songs = mutableListOf<Song>()
    private var observers = RemoteCallbackList<IPlayObserver>()
    private var infoModel: SongInfoModel = SongInfoModelImpl()
    private var daoModel: DownloadSongModel = DownloadSongModelImpl()
    private var broadcastIng: AtomicBoolean = AtomicBoolean(false)

    /**
     * 当前的播放状态.
     * */
    private var currentState = Constant.PlaySongConstant.PLAY_STATE_STOP

    /**
     * 定时任务.
     * */
    private var seekTimerTask: SeekTimeTask? = null

    /**
     * 标志是否是第一次播放.
     * */
    private var firstPlay = true

    /**
     * 记录是否调用了MediaPlayer的reset()方法
     * */
    private var hasReset = false

    /**
     * 记录当前播放的歌曲在list中的位置.
     * */
    private var currentPosition = -1

    /**
     * 当前的播放模式.
     * */
    private var playMode = ORDER_PLAY

    /**
     * 标记是否开启了定时任务.
     * */
    private var startTimer = false

    /**
     * 当前歌曲播放的位置.
     * */
    private var currentTimePoint = 0

    private val listener: SongInfoModel.OnSongPlayInfoListener =
        object : SongInfoModel.OnSongPlayInfoListener {
            override fun getSongPlayInfoSuccess(song: Song) {
                performSong(song.url ?: NULL_URL)
            }

            override fun error(msg: String) {
                showText("歌曲无法播放，自动切换下一首")
                playNextSong()
            }
        }

    override fun onCreate() {
        super.onCreate()
        initMediaPlayer()
        NotifyManager.init(this)
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private fun initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        mediaPlayer?.let {
            // todo 增加监听
            it.setOnPreparedListener { mp ->
                if (mp != null) {
                    NotifyManager.showCtrlView()
                    mp.start()
                    onSongChange()
                    currentState = PLAY_STATE_PLAY
                    onPlayStateChange()
                    if (!startTimer) {
                        startTimer()
                    }
                }
            }

            it.setOnCompletionListener { _ ->
                playNextSong()
            }

            it.setOnErrorListener { mp, what, extra ->
                LogUtil.d(TAG, "onError")
                true
            }

//            it.setDataSource("http://m7.music.126.net/20200923214555/305d3b56a7dcdb862fb487a4f6584f07/ymusic/071d/ca5f/ab5b/97adc83065f23caf6f4b409e961f0f0c.mp3")
//            it.prepareAsync()
        }
    }

    private fun loadSong(song: Song) {
        //在线歌曲
        if (song.online) {
            song.id?.let { songId ->
                daoModel.querySongPath(songId, object : DownloadSongModel.OnListener {
                    override fun querySongPathSuccess(path: String?) {
//                        LogUtil.d("abcde","查询到的path为空？${path == null}")
                        path?.let {
                            if (File(it).exists()) {
                                performSong(it)
                                return
                            }
                        }

                        //如果url是空，那么去请求url的数据
                        if (song.url == null) {
                            infoModel.getSongPlayInfo(song, listener)
                            return
                        }

                        //如果url不为空，那么请求歌曲的播放
                        song.url?.let {
                            performSong(it)
                        }
                    }
                })
            }
            //本地歌曲
        } else {
            song.url?.let {
                performSong(it)
                return
            }

            if (song.url == null) {
                playNextSong()
            }
        }

    }

    override fun onDestroy() {
        mediaPlayer?.let {
            it.stop()
            it.release()
            mediaPlayer = null
        }
        NotifyManager.closeCtrlView()
        super.onDestroy()
    }

    private fun onPlayStateChange() {
        //如果当前正在广播
        if (broadcastIng.get()) {
            return
        }
        //当前没有在广播
        broadcastIng.set(true)
        //遍历观察者，通知播放状态的改变.
        val size = observers.beginBroadcast()
        for (i in 0 until size) {
            val observer = observers.getBroadcastItem(i)
            observer.onPlayStateChange(currentState)
        }
        observers.finishBroadcast()
        broadcastIng.set(false)
    }

    private fun onSeekChange() {
        //如果当前正在广播
        if (broadcastIng.get()) {
            return
        }
        //当前没有在广播
        broadcastIng.set(true)
        //遍历观察者
        val size = observers.beginBroadcast()
        for (i in 0 until size) {
            val observer = observers.getBroadcastItem(i)
            observer.onSeekChange(currentTimePoint)
        }
        observers.finishBroadcast()
        broadcastIng.set(false)
    }

    private fun onSongChange() {
        //如果当前正在广播
        if (broadcastIng.get()) {
            return
        }
        //当前没有在广播
        broadcastIng.set(true)
        //遍历观察者
        val size = observers.beginBroadcast()
        for (i in 0 until size) {
            val observer = observers.getBroadcastItem(i)
            observer.onSongChange(songs[currentPosition], currentPosition)
        }
        observers.finishBroadcast()
        broadcastIng.set(false)
    }

    private fun onSongsEmpty() {
        //如果当前正在广播
        if (broadcastIng.get()) {
            return
        }
        //当前没有在广播
        broadcastIng.set(true)
        //遍历观察者
        val size = observers.beginBroadcast()
        for (i in 0 until size) {
            val observer = observers.getBroadcastItem(i)
            observer.onSongsEmpty()
        }
        observers.finishBroadcast()
        broadcastIng.set(false)
    }

    private var binder = object : IPlayInterface.Stub() {
        override fun playSongs(songs: MutableList<Song>?, position: Int) {
            this@PlayService.playSongs(songs, position)
        }

        override fun getDuration(): Int {
            mediaPlayer?.let {
                return it.duration
            }
            return 0
        }

        override fun download(song: Song?) {
            song?.let {
                //下载过程的监听器
                val downloadListener = object : SimpleDownloadListener() {
                    override fun onProgress(progress: Int) {
                        NotifyManager.downloadProgress(it.id, it.name, progress)
                    }

                    override fun onSuccess(localPath: String) {
                        showText("歌曲 ${it.name} 下载成功")
                        NotifyManager.closeNotify(it.id)
                        //将数据插入数据库中
                        it.url = localPath
                        daoModel.insert(it)
//                        LogUtil.d("abcde","路径：${localPath}")
                    }

                    override fun onFailed() {
                        showText("歌曲 ${it.name} 下载失败")
                        NotifyManager.closeNotify(it.id)
                    }
                }

                //如果之前已经为该Song请求过url,已经有数据了
                it.url?.let { url ->
                    if (url != NULL_URL) {
                        DownloadUtil.downloadSong(it.name ?: "", it.url ?: "", downloadListener)
                    } else {
                        showText("资源错误")
                    }
                    return
                }

                //url为null，说明之前没有为该song请求过数据.
                infoModel.getSongPlayInfo(it, object : SongInfoModel.OnSongPlayInfoListener {
                    //请求数据成功
                    override fun getSongPlayInfoSuccess(song: Song) {
                        if (song.url != NULL_URL) {
                            ThreadUtil.runOnThreadPool(Runnable {
                                DownloadUtil.downloadSong(
                                    song.name ?: "", song.url ?: "", downloadListener
                                )
                            })
                        } else {
                            showText("资源错误")
                        }
                    }

                    //请求数据失败
                    override fun error(msg: String) {
                        showText("请求数据失败")
                    }
                })
            }
        }

        override fun registerObserver(observer: IPlayObserver?) {
            observers.register(observer)
        }

        override fun seekTo(seek: Int) {
            this@PlayService.seekTo(seek)
        }

        override fun getCurrentPosition(): Int {
            return this@PlayService.currentPosition
        }

        override fun getPlayState(): Int {
            return this@PlayService.currentState
        }

        override fun getCurrenPoint(): Int {
            mediaPlayer?.let {
                return it.currentPosition
            }
            return 0
        }

        override fun unregisterObserver(observer: IPlayObserver?) {
            observers.unregister(observer)
        }

        override fun playOrPause() {
            this@PlayService.playOrPause()
        }

        override fun getCurrentSong(): Song? {
            return if (currentPosition >= 0 && currentPosition < songs.size) {
                songs[currentPosition]
            } else {
                null
            }
        }

        override fun playPre() {
            playPreSong()
        }

        override fun addNext(song: Song?) {
            this@PlayService.addNext(song)
        }

        override fun removeSong(position: Int) {
            //如果移除的歌曲在当前播放的歌曲的前面
            if (position < this@PlayService.currentPosition) {
                songs.removeAt(position)
                this@PlayService.currentPosition--
                //如果移除的歌曲在当前播放的歌曲的后面
            } else if (position > this@PlayService.currentPosition) {
                songs.removeAt(position)
                //如果移除的歌曲正在播放
            } else {
                songs.removeAt(position)
                //移除后还有歌
                if (songs.isNotEmpty()) {
                    when (playMode) {
                        //列表循环播放
                        ORDER_PLAY, LOOP_PLAY -> {
                            if (currentPosition == songs.size) {
                                currentPosition = 0
                            }
                        }
                        //随机播放
                        RANDOM_PLAY -> currentPosition = (Math.random() * songs.size).toInt()
                    }
                    loadSong(songs[currentPosition])
                    //移除后已经没有歌曲了
                } else {
                    resetInfo()
                    onSongsEmpty()
                }
            }
        }

        override fun getQueueSongs(): MutableList<Song> {
            return songs
        }

        override fun setCurrentPosition(currentPosition: Int) {
            this@PlayService.currentPosition = currentPosition
        }

        override fun getPlayMode(): Int {
            return this@PlayService.playMode
        }

        override fun playNext() {
            playNextSong()
        }

        override fun setPlayMode(mode: Int) {
            this@PlayService.playMode = mode
        }

        override fun playSongByIndex(position: Int) {
            if (position != currentPosition) {
                currentPosition = position
                loadSong(songs[currentPosition])
            }
        }
    }

    private fun addNext(song: Song?) {
        song?.let {
            if (songs.isEmpty()) {
                songs.add(it)
                currentPosition = 0
                loadSong(it)
            } else {
                songs.add(currentPosition + 1, it)
            }
        }

    }

    private fun playOrPause() {
        if (songs.size == 0) {
            //todo 给点没有歌的提示
            return;
        }
        when (currentState) {
            PLAY_STATE_PLAY -> {
                mediaPlayer?.let {
                    it.pause()
                    currentState = PLAY_STATE_PAUSE
                    stopTimer()
                }
            }
            PLAY_STATE_PAUSE -> {
                mediaPlayer?.let {
                    it.start()
                    currentState = PLAY_STATE_PLAY
                    startTimer()
                }
            }
        }
        //遍历观察者，通知播放状态的改变.
        onPlayStateChange()
    }

    /**
     * 这里要加锁进行同步，防止被频繁调用而报错.
     * */
    @Synchronized
    private fun performSong(dataSource: String) {
        if (dataSource == NULL_URL) {
            ThreadUtil.runOnUi(Runnable { showText("歌曲无法播放，自动切换下一首") })
            playNextSong()
            return
        }

        if (mediaPlayer == null) {
            initMediaPlayer()
        }

        //若音乐正在播放并且定时任务开着，那么关闭定时任务
        if (currentState == PLAY_STATE_PLAY && startTimer) {
            stopTimer()
        }

        LogUtil.d(TAG,"dataSource:${dataSource}")
        
        mediaPlayer?.let {
            hasReset = true
            it.reset()
            it.setDataSource(dataSource)
            it.prepareAsync()
            //todo 这里少了个pause()
        }
    }

    private fun playNextSong() {
        if (songs.size == 0) {
            //todo 给点没歌的提示
            return
        }

        when (playMode) {
            //列表循环播放
            ORDER_PLAY -> currentPosition = (currentPosition + 1) % songs.size
            //随机播放
            RANDOM_PLAY -> currentPosition = (Math.random() * songs.size).toInt()
            ////单曲循环不用做处理
            else -> {
            }
        }
        loadSong(songs[currentPosition])
//        performSong(songs[currentPosition].url ?: NULL_URL)
    }

    private fun playPreSong() {
        if (songs.size == 0) {
            //todo 给点没歌的提示
            return
        }

        when (playMode) {
            //列表循环播放
            ORDER_PLAY -> if (currentPosition == 0) {
                currentPosition = songs.size - 1
            } else {
                currentPosition--
            }
            //随机播放
            RANDOM_PLAY -> currentPosition = (Math.random() * songs.size).toInt()
            //单曲循环不用做处理
            else -> {
            }
        }
        loadSong(songs[currentPosition])
//        performSong(songs[currentPosition].url ?: NULL_URL)
    }

    private fun playSongs(songList: MutableList<Song>?, position: Int) {
        LogUtil.d(TAG, "$position")
        songList?.let {
            songs.clear()
            songs.addAll(it)
            currentPosition = position

//            performSong(songs[position].url ?: NULL_URL)
            loadSong(songs[currentPosition])
        }
    }

    private fun seekTo(seek: Int) {
        mediaPlayer?.seekTo(seek)
    }

    private fun startTimer() {
        if (timer == null) {
            timer = Timer()
        }
        if (seekTimerTask == null) {
            seekTimerTask = SeekTimeTask()
        }
        timer?.schedule(seekTimerTask, 0, 100)
        startTimer = true
    }

    private fun stopTimer() {
        timer?.let {
            it.cancel()
            timer = null
        }

        seekTimerTask?.let {
            it.cancel()
            seekTimerTask = null
        }

        startTimer = false
    }

    private fun showText(msg: String) {
        ThreadUtil.runOnUi(Runnable { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() })
    }

    /**
     * 重置信息.
     * */
    private fun resetInfo() {
        //若音乐正在播放并且定时任务开着，那么关闭定时任务
        if (currentState == PLAY_STATE_PLAY && startTimer) {
            stopTimer()
        }
        currentPosition = -1
        currentState = PLAY_STATE_STOP
        currentTimePoint = 0
        mediaPlayer?.reset()
    }

    private inner class SeekTimeTask : TimerTask() {
        override fun run() {
            mediaPlayer?.let {
                //播放进度，用于控制进度条
                currentTimePoint = it.currentPosition
                //遍历观察者
                onSeekChange()
            }
        }
    }

}
