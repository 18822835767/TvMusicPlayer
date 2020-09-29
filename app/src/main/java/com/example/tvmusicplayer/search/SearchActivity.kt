package com.example.tvmusicplayer.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.bean.Song

class SearchActivity : AppCompatActivity(),SearchContract.OnView{

    private lateinit var toolBar: Toolbar
    private lateinit var searchView : SearchView
    private lateinit var loadingFl : FrameLayout
    private lateinit var presenter : SearchContract.Presenter

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()
        initData()
        setActionBar()
    }

    private fun initView() {
        toolBar = findViewById(R.id.search_toolbar)
        loadingFl = findViewById(R.id.fl_loading)
    }

    private fun initData() {
        //构造presenter
        SearchPresenter(this)
    }

    /**
     * 设置toolbar的相关信息.
     * */
    private fun setActionBar() {
        setSupportActionBar(toolBar)
        //toolbar左侧显示返回按钮
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        menu?.let {  
            val menuItem = it.findItem(R.id.search_view)
            menuItem?.let { item ->  
                searchView = item.actionView as SearchView
                //模式情况下，不是只显示图标
                searchView.setIconifiedByDefault(false)
                
                //todo 增加监听
            }
        }
        
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return true
    }

    override fun searchSuccess(list: MutableList<Song>) {
        TODO("Not yet implemented")
    }

    override fun setPresenter(presenter: SearchContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
        loadingFl.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingFl.visibility = View.GONE
    }

    override fun showError(errorMessage: String) {
        Toast.makeText(this,"错误：$errorMessage", Toast.LENGTH_SHORT).show()
    }
}
