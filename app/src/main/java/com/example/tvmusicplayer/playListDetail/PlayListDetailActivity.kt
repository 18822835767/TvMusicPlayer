package com.example.tvmusicplayer.playListDetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.adapter.PlayListDetailAdapter
import com.example.tvmusicplayer.bean.PlayList
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.util.LogUtil

/**
 * 点击歌单时，展示歌单中的歌曲的活动.
 * */
class PlayListDetailActivity : AppCompatActivity(),PlayListDetailContract.OnView {
    
    private val TAG = "PlayListDetailActivity"
    private var playList : PlayList? = null
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : PlayListDetailAdapter
    
    companion object{
        const val PLAY_LIST_PARAMS = "play_list_params"
        
        fun actionStart(platList : PlayList,context: Context){
            val intent = Intent(context,PlayListDetailActivity::class.java)
            intent.putExtra(PLAY_LIST_PARAMS,platList)
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
    }
    
    private fun initData(){
        PlayListDetailPresenter(this)
        
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
    }

    override fun setPresenter(presenter: PlayListDetailContract.Presenter) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMessage: String) {
    }
}
