package com.example.tvmusicplayer.playlist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.bean.PlayList
import com.example.tvmusicplayer.util.LoginStatusManager

class UserPlayListActivity : AppCompatActivity(),UserPlayListContract.OnView{

    private lateinit var presenter : UserPlayListContract.Presenter
    private val TAG = "UserPlayListActivity"
    
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
    }
    
    private fun initView(){
        
    }
    
    private fun initData(){
        UserPlayListPresenter(this)
        LoginStatusManager.user?.id?.let { presenter.getUserPlayList(it) }
    }
    
    override fun getUserPlayListSuccess(playLists: MutableList<PlayList>) {
        playLists.forEach{
            it.name?.let {
                Log.d(TAG,it)
            }
        }
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
