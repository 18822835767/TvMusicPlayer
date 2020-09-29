package com.example.tvmusicplayer.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tvmusicplayer.R

class SearchActivity : AppCompatActivity() {

    companion object{
        fun actionStart(context : Context){
            val intent = Intent(context,SearchActivity::class.java)
            context.startActivity(intent)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }
}
