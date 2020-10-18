package com.example.tvmusicplayer.lcoal

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.adapter.LocalAdapter
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.service.PlayServiceManager
import com.example.tvmusicplayer.util.Constant
import com.example.tvmusicplayer.util.LogUtil
import com.example.tvmusicplayer.util.PermissionHelper
import com.example.tvmusicplayer.util.ThreadUtil

class LocalActivity : AppCompatActivity(), LocalContract.OnView,
    BaseRecyclerViewAdapter.OnItemClickListener, LocalAdapter.OnPopupClickListener,
    AdapterView.OnItemClickListener {

    private val TAG = "LocalActivity"
    private val PERMISSION_REQUEST_CODE = 0

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var presenter: LocalContract.Presenter
    private lateinit var adapter: LocalAdapter
    private lateinit var manager: LinearLayoutManager
    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var popupAdapter: ArrayAdapter<String>

    /**
     * 记录点击的popup的Song在LocalAdapter中的位置.
     * */
    private var popupItemClickPosition = -1

    private val popupArray = arrayOf(Constant.PopupWindowConstant.NEXT_PAY)

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

        //设置RecyclerView的数据
        adapter = LocalAdapter(mutableListOf<Song>(), R.layout.local_song_item)
        adapter.setItemClickListener(this)
        adapter.setOnPopupClickListener(this)
        manager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //设置弹窗数据
        listPopupWindow = ListPopupWindow(this)
        popupAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, popupArray)
        listPopupWindow.setAdapter(popupAdapter)
        //设置宽度和高度
        val density = this.resources.displayMetrics.density.toInt()//值是3.0
        val itemHeight =
            this.resources.getDimensionPixelOffset(R.dimen.popup_item_height)//55dp->165
        listPopupWindow.setContentWidth((125 * density))
        listPopupWindow.height = popupArray.size * itemHeight
//        listPopupWindow.horizontalOffset = (-25 * density) 这句话好像没作用
        listPopupWindow.isModal = true
        listPopupWindow.setOnItemClickListener(this)

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
        } else {
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
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initData()
            } else {
                Toast.makeText(this, "拒绝权限将无法使用该功能", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun getLocalSongsSuccess(songs: MutableList<Song>) {
        songs.sort()
//        songs.forEach { song -> LogUtil.d(TAG,song.toString()) }
        adapter.addDatas(songs)
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

    override fun onItemClick(v: View?, position: Int) {
        ThreadUtil.runOnThreadPool(Runnable {
            PlayServiceManager.playSongs(
                adapter.getItems(),
                position
            )
        })
    }

    override fun onPopupClick(v: View?, position: Int) {
//        Toast.makeText(this,"点击$position",Toast.LENGTH_SHORT).show()
        v?.let {
            popupItemClickPosition = position
            listPopupWindow.anchorView = it
            listPopupWindow.show()
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        Toast.makeText(this,"点击${popupArray[position]}",Toast.LENGTH_SHORT).show()
        listPopupWindow.dismiss()

        if (popupArray[position] == Constant.PopupWindowConstant.NEXT_PAY) {
            if (popupItemClickPosition != -1) {
                PlayServiceManager.addNext(adapter.getItem(popupItemClickPosition))
            }
        }
    }

}
