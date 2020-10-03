package com.example.tvmusicplayer.lcoal

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.util.Constant
import com.example.tvmusicplayer.util.LogUtil
import com.example.tvmusicplayer.util.PermissionHelper

class LocalActivity : AppCompatActivity(),LocalContract.OnView {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private val PERMISSION_REQUEST_CODE = 0
    private lateinit var presenter: LocalContract.Presenter
    private val TAG = "LocalActivity"

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
        setActionBar()
        requestPermissions()
//        initData()
//        LogUtil.d(TAG,"onCreate...")
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recycler_view)
    }

    private fun initData() {
        LocalPresenter(this)
        
        presenter.getLocalSongs()
    }

    private fun setActionBar() {
        toolbar.title = Constant.UserFragmentConstant.LOCAL_MUSIC
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun requestPermissions() {
        if (!PermissionHelper.permissionAllow(
                this, arrayOf<String>(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        ) {
            PermissionHelper.requestPermissions(
                this, arrayOf<String>(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), PERMISSION_REQUEST_CODE
            )
        }else{
            initData()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               initData()
            }else{
                Toast.makeText(this, "拒绝权限将无法使用该功能", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun getLocalSongsSuccess(songs: MutableList<Song>) {
        
    }

    override fun setPresenter(presenter: LocalContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMessage: String) {
    }
}
