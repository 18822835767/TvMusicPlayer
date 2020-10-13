package com.example.tvmusicplayer.recommend

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
import com.example.tvmusicplayer.adapter.RecommendNewSongAdapter
import com.example.tvmusicplayer.adapter.RecommendPlayListAdapter
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.bean.CircleButtonBean
import com.example.tvmusicplayer.bean.PlayList
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.playListDetail.PlayListDetailActivity
import com.example.tvmusicplayer.util.LogUtil
import com.example.tvmusicplayer.widget.BannerViewPager

class RecommendFragment : Fragment(), BaseRecyclerViewAdapter.OnItemClickListener,
    RecommendContract.OnView {

    private val TAG = "RecommendFragment"
    private lateinit var banner: BannerViewPager
    private lateinit var circleButtonRv: RecyclerView
    private lateinit var circleButtonAdapter: CircleButtonAdapter
    private lateinit var presenter: RecommendContract.Presenter
    private lateinit var playListRv : RecyclerView
    private lateinit var playListAdapter : RecommendPlayListAdapter
    private lateinit var newSongRv : RecyclerView
    private lateinit var newSongAdapter : RecommendNewSongAdapter
    
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
        playListRv = view.findViewById(R.id.recommend_playlist_rv)
        newSongRv = view.findViewById(R.id.recommend_newsong_rv)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initData()
    }

    private fun initData() {
        RecommendPresenter(this)
        presenter.getBanner(1)
        presenter.getRecommendPlayList(10)
        presenter.getRecommendNewSong()
        
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
        val circleButtonManager = LinearLayoutManager(context)
        circleButtonManager.orientation = LinearLayoutManager.HORIZONTAL
        circleButtonRv.adapter = circleButtonAdapter
        circleButtonRv.layoutManager = circleButtonManager
        
        //推荐歌单
        playListAdapter = RecommendPlayListAdapter(mutableListOf(),R.layout.recommend_playlist_item)
        //设置监听
        playListAdapter.setItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(v: View?, position: Int) {
                context?.let {
                    val playList = playListAdapter.getItem(position)
                    PlayListDetailActivity.actionStart(playList,it)
                }
            }
        })
        val playListManager = LinearLayoutManager(context)
        playListManager.orientation = LinearLayoutManager.HORIZONTAL
        playListRv.adapter = playListAdapter
        playListRv.layoutManager = playListManager
        
        //推荐新歌曲
        newSongAdapter = RecommendNewSongAdapter(mutableListOf(),R.layout.recommend_newsong_item)
        //设置监听
        newSongAdapter.setItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(v: View?, position: Int) {
                
            }

        })
        val newSongManager = LinearLayoutManager(context)
        newSongManager.orientation = LinearLayoutManager.HORIZONTAL
        newSongRv.adapter = newSongAdapter
        newSongRv.layoutManager = newSongManager
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

    override fun getRecommendPlayListSuccess(list: MutableList<PlayList>) {
        playListAdapter.clearAndAddNewDatas(list)
//        list.forEach { 
//            LogUtil.d(TAG,"id:${it.id},name:${it.name},picUrl:${it.coverImgUrl}")
//        }
    }

    override fun getRecommendNewSongSuccess(list: MutableList<Song>) {
        newSongAdapter.clearAndAddNewDatas(list)
//        list.forEach { song -> LogUtil.d(TAG,song.toString()) }
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
