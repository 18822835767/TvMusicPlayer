package com.example.tvmusicplayer.main

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
import com.example.tvmusicplayer.login.LoginActivity
import com.example.tvmusicplayer.recommend.RecommendFragment
import com.example.tvmusicplayer.user.UserFragment
import com.example.tvmusicplayer.manager.LoginStatusManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var drawerLyout: DrawerLayout
    private lateinit var navView: NavigationView
    private var fragments = mutableListOf<Fragment>()

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
        drawerLyout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
    }

    private fun initData() {
        fragments.add(UserFragment())
        fragments.add(RecommendFragment())

        viewPager.adapter = object : FragmentStatePagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment {
                return fragments.get(position)
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }
        //设置联动
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
    }

    fun initEvent() {
        navView.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.nav_login -> {
                        if (LoginStatusManager.alreadyLogin) {
                            Toast.makeText(this@MainActivity, "已经登陆过啦", Toast.LENGTH_SHORT).show()
                        } else {
                            LoginActivity.actionStart(this@MainActivity)
                        }
                    }
                }
                return true
            }
        })
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
            android.R.id.home -> drawerLyout.openDrawer(GravityCompat.START)
        }
        return true
    }
}
