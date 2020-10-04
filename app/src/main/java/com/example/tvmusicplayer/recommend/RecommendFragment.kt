package com.example.tvmusicplayer.recommend

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.adapter.CircleButtonAdapter
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.bean.CircleButtonBean
import com.example.tvmusicplayer.widget.BannerViewPager

class RecommendFragment : Fragment(), BaseRecyclerViewAdapter.OnItemClickListener,
    RecommendContract.OnView {

    private lateinit var banner: BannerViewPager
    private lateinit var circleButtonRv: RecyclerView
    private lateinit var circleButtonAdapter: CircleButtonAdapter
    private lateinit var presenter: RecommendContract.Presenter

    private var circleButtonText = mutableListOf<CircleButtonBean>()

    companion object {
        @JvmStatic
        fun newInstance(): RecommendFragment {
            return RecommendFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recommend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        banner = view.findViewById(R.id.banner_view_pager)
        circleButtonRv = view.findViewById(R.id.circle_button_rv)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initData()
    }

    private fun initData() {
        RecommendPresenter(this)
        presenter.getBanner(1)
        
        //推荐面的一排圆形按钮
        circleButtonText.add(CircleButtonBean("每日推荐", R.drawable.ic_tuijian))
        circleButtonText.add(CircleButtonBean("私人FM", R.drawable.ic_fm))
        circleButtonText.add(CircleButtonBean("歌单", R.drawable.ic_gedan))
        circleButtonText.add(CircleButtonBean("排行榜", R.drawable.ic_paihangbang))
        circleButtonText.add(CircleButtonBean("直播", R.drawable.ic_zhibo))
        circleButtonText.add(CircleButtonBean("电台", R.drawable.ic_diantai))
        circleButtonText.add(CircleButtonBean("数字专辑", R.drawable.ic_shuzizhuangji))
        circleButtonText.add(CircleButtonBean("游戏专区", R.drawable.ic_game))
        circleButtonAdapter = CircleButtonAdapter(circleButtonText, R.layout.circle_button_item)
        circleButtonAdapter.setItemClickListener(this)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        circleButtonRv.adapter = circleButtonAdapter
        circleButtonRv.layoutManager = manager
    }

    override fun onItemClick(v: View?, position: Int) {
        val circleButtonBean = circleButtonAdapter.getItem(position)
        circleButtonBean.text?.let {
            Toast.makeText(context, "Click：${it.trim()}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getBannerSuccess(list: MutableList<String>) {
        banner.setData(list)
    }

    override fun setPresenter(presenter: RecommendContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}
