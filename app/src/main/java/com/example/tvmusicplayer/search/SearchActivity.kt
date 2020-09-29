package com.example.tvmusicplayer.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.example.tvmusicplayer.R

class SearchActivity : AppCompatActivity(){

    private lateinit var toolBar: Toolbar
    private lateinit var searchView : SearchView
    private lateinit var searchFragment: SearchFragment
    
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
    }

    private fun initData() {
        //初始化展示搜索内容的碎片
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        searchFragment = SearchFragment.newInstance()
        transaction.replace(R.id.fragment_layout,searchFragment)
        transaction.commit()
    }

    /**
     * 设置toolbar的相关信息.
     * */
    private fun setActionBar() {
        toolBar.title=""
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
                
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query?.let{
                            //搜索相关内容
                            searchFragment.searchContent(it)
                        }
                        
                        //提交后失去焦点，即收起软键盘
                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
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
}
