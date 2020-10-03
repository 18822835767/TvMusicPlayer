package com.example.tvmusicplayer.lcoal

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.util.Constant

class LocalActivity : AppCompatActivity() {

    private lateinit var toolbar : Toolbar
    private lateinit var recyclerView: RecyclerView
    
    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, LocalActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local)
    
        initView()
        initData()
        setActionBar()
    }
    
    private fun initView(){
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recycler_view)
    }
    
    private fun initData(){
        
    }
    
    private fun setActionBar(){
        toolbar.title= Constant.UserFragmentConstant.LOCAL_MUSIC
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
}
