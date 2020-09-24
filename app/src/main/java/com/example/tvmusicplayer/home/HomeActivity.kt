package com.example.tvmusicplayer.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.bottomPlayer.BottomPlayerFragment
import com.example.tvmusicplayer.login.LoginActivity
import com.example.tvmusicplayer.recommend.RecommendFragment
import com.example.tvmusicplayer.user.UserFragment
import com.example.tvmusicplayer.manager.LoginStatusManager
import com.example.tvmusicplayer.util.NetWorkUtil
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity(),HomeContract.OnView {

    private var toolbar: Toolbar? = null
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private var fragments = mutableListOf<Fragment>()
    private lateinit var presenter : HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initView()
        initData()
        initEvent()
        setActionBar()
    }


    private fun initView() {
        toolbar = findViewById(R.id.main_toolbar)
        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
    }

    private fun initData() {
        HomePresenter(this)
//        presenter.getLoginStatus()//获取登录状态
        
        fragments.add(UserFragment.newInstance())
        fragments.add(RecommendFragment.newInstance())

        viewPager.adapter = object : FragmentStatePagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }
        //设置联动
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
    }

    private fun initEvent() {
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_login -> {
                    if (LoginStatusManager.alreadyLogin) {
                        Toast.makeText(this@HomeActivity, "已经登陆过啦~~", 
                            Toast.LENGTH_SHORT).show()
                    } else {
                        LoginActivity.actionStart(this@HomeActivity)
                    }
                }
                R.id.nav_logout -> {
                    if(!LoginStatusManager.alreadyLogin){
                        Toast.makeText(this@HomeActivity,"当前还没有登陆呢~~",
                            Toast.LENGTH_SHORT).show()
                    }else{
                        if(!NetWorkUtil.isNetWorkConnected(this@HomeActivity)){
                            Toast.makeText(this@HomeActivity, "当前没有网络噢~~", 
                                Toast.LENGTH_SHORT).show()
                        }else{
                            presenter.logout()
                            Toast.makeText(this@HomeActivity, "退出登陆成功",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
            true
        }
        
    }

    private fun setActionBar() {
        toolbar?.title = ""
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_navi)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show()
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    override fun setPresenter(presenter: HomeContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMessage: String) {
    }
}
