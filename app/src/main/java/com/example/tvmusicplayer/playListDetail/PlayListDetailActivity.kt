package com.example.tvmusicplayer.playListDetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.adapter.PlayListDetailAdapter
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.bean.PlayList
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.service.PlayServiceManager
import com.example.tvmusicplayer.util.LogUtil
import com.example.tvmusicplayer.util.ThreadUtil
import com.example.tvmusicplayer.widget.LettersNavi
import com.squareup.picasso.Picasso
import java.util.*

/**
 * 点击歌单时，展示歌单中的歌曲的活动.
 * */
class PlayListDetailActivity : AppCompatActivity(),PlayListDetailContract.OnView,
    BaseRecyclerViewAdapter.OnItemClickListener,LettersNavi.OnTouchLetterListener{
    
    private val TAG = "PlayListDetailActivity"
    private var playList : PlayList? = null
    private lateinit var presenter : PlayListDetailContract.Presenter
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : PlayListDetailAdapter
    private lateinit var loadingLayout : FrameLayout
    private lateinit var playListCoverIv : ImageView
    private lateinit var playlistNameTv : TextView
    private lateinit var manager : LinearLayoutManager
    private lateinit var lettersNavi: LettersNavi
    
    companion object{
        const val PLAY_LIST_PARAMS = "play_list_params"
        
        fun actionStart(playList : PlayList, context: Context){
            val intent = Intent(context,PlayListDetailActivity::class.java)
            intent.putExtra(PLAY_LIST_PARAMS,playList)
            context.startActivity(intent)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list_detail)
    
        //获取传入的实体类
        playList = intent.getParcelableExtra(PLAY_LIST_PARAMS)
        
        initView()
        initData()
        setActionBar()
    }
    
    private fun initView(){
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recycler_view)
        loadingLayout = findViewById(R.id.fl_loading)
        playListCoverIv = findViewById(R.id.playlist_cover_iv)
        playlistNameTv = findViewById(R.id.playlist_name_tv)
        lettersNavi = findViewById(R.id.letters_navi)
    }
    
    private fun initData(){
        PlayListDetailPresenter(this)
        
        //设置RecyclerView的数据
        adapter = PlayListDetailAdapter(mutableListOf<Song>(),R.layout.song_item)
        adapter.setItemClickListener(this)
        manager = LinearLayoutManager(this)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        //添加分割线
        recyclerView.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        
        //设置歌单名字和图片在相应控件上
        playList?.let { 
            playlistNameTv.text = it.name 
            Picasso.get().load(it.coverImgUrl)
                .resize(100,100)
                .placeholder(R.drawable.empty_photo)
                .error(R.drawable.load_error)
                .into(playListCoverIv)
        }
        
        //获取歌单中的歌曲数据
        playList?.id?.let {presenter.getPlayListDetail(it)}
    
        lettersNavi.setListener(this)
    }
    
    
    private fun setActionBar(){
        playList?.let { 
            it.name?.let { name ->
                toolbar.title = name    
            }
        }
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }

    override fun getPlayListDetailSuccess(list: MutableList<Song>) {
        list.sort()
        adapter.addDatas(list)
//        list.forEach { song -> LogUtil.d(TAG,song.toString()) }
    }

    override fun setPresenter(presenter: PlayListDetailContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingLayout.visibility = View.GONE
    }

    override fun showError(errorMessage: String) {
        Toast.makeText(this,"错误：$errorMessage",Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(v: View?, position: Int) {
        ThreadUtil.runOnThreadPool(Runnable {PlayServiceManager.playSongs(adapter.getItems(),position)})
    }

    override fun touchLetterListener(s: String) {
        if(s.isNotEmpty()){
            val position = adapter.getSelectPosition(s.first())
            if(position != -1){
                manager.scrollToPositionWithOffset(position,0)
            }
        }
    }
}
