package com.example.tvmusicplayer.playlist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.adapter.UserPlayListAdapter
import com.example.tvmusicplayer.bean.PlayList
import com.example.tvmusicplayer.manager.LoginStatusManager

class UserPlayListActivity : AppCompatActivity(),UserPlayListContract.OnView{

    private val TAG = "UserPlayListActivity"
    private lateinit var presenter : UserPlayListContract.Presenter
    private lateinit var toolbar: Toolbar
    private lateinit var listRecyclerView: RecyclerView
    private lateinit var adapter : UserPlayListAdapter
    private var playLists = mutableListOf<PlayList>()
    
    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context,UserPlayListActivity::class.java)
            context.startActivity(intent)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list)
    
        initView()
        initData()
        setActionBar()
    }
    
    private fun initView(){
        toolbar = findViewById(R.id.playlist_toolbar)
        listRecyclerView = findViewById(R.id.list_recycler_view)
    }
    
    private fun initData(){
        //初始化Presenter
        UserPlayListPresenter(this)
        //获取用户的歌单数据
        LoginStatusManager.user?.id?.let { presenter.getUserPlayList(it) }
        
        //设置RecyclerView的数据
        adapter = UserPlayListAdapter(playLists,R.layout.playlist_item)
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        listRecyclerView.adapter = this.adapter
    }
    
    private fun setActionBar(){
        toolbar.title = "我的歌单"
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

    override fun getUserPlayListSuccess(playLists: MutableList<PlayList>) {
        
    }

    override fun setPresenter(presenter: UserPlayListContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
        
    }

    override fun showError(errorMessage: String) {
        
    }
}
