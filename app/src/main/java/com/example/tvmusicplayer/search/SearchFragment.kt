package com.example.tvmusicplayer.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.adapter.SearchAdapter
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.bean.Song
import com.example.tvmusicplayer.service.PlayServiceManager
import com.example.tvmusicplayer.util.Constant.SearchSongConstant.SEARCH_TYPE
import com.example.tvmusicplayer.util.LogUtil
import com.example.tvmusicplayer.util.ThreadUtil

class SearchFragment : Fragment(), SearchContract.OnView,
    BaseRecyclerViewAdapter.OnItemClickListener {

    private val TAG = "SearchFragment"

    private lateinit var loadingFl: FrameLayout
    private lateinit var presenter: SearchContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var manager: LinearLayoutManager

    /**
     * 分页加载的数量.
     * */
    private var pageSize: Int = 20

    /**
     * 记录搜索界面当前是在第几面
     * */
    private var currentPage: Int = 0

    /**
     * 上拉加载更多时，是否已加载歌曲结束的标志
     */
    private var loadingFinishFlag: Boolean = true

    /**
     * 记录搜索的歌曲还有多少未被加载.
     * */
    private var remainingCount: Int = 0

    /**
     * 记录最新搜索的关键词.
     * */
    private var lastKeyWord: String = ""

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingFl = view.findViewById(R.id.fl_loading)
        recyclerView = view.findViewById(R.id.recycler_view)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initData()
        initEvent()
    }

    private fun initData() {
        //构造presenter
        SearchPresenter(this)

        adapter = SearchAdapter(mutableListOf<Song>(), R.layout.search_item)
        adapter.setItemClickListener(this)
        manager = LinearLayoutManager(context)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        //添加分割线
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initEvent() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val manager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                //当停止滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //得到最后一个完全可见的item的下标
                    val lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()
                    val totalItemCount = manager.itemCount
                    //已经到最后一个
                    if (lastVisibleItem == totalItemCount - 1) {
                        //此处是为了防止多次加载，表示当前没有在"加载更多歌曲".
                        if (loadingFinishFlag) {
                            //未加载的歌曲充足的情况下
                            if (remainingCount >= pageSize) {
                                loadingFinishFlag = false
                                //设置FooterView
                                setFooterView(recyclerView)
                                presenter.loadMoreSongs(
                                    pageSize, currentPage * pageSize,
                                    SEARCH_TYPE, lastKeyWord
                                )
                                //未加载的歌曲数量不足一页的情况下
                            } else if (remainingCount > 0) {
                                loadingFinishFlag = false
                                //设置FooterView
                                setFooterView(recyclerView)
                                presenter.loadMoreSongs(
                                    remainingCount, currentPage * pageSize,
                                    SEARCH_TYPE, lastKeyWord
                                )
                                remainingCount = 0
                            }
                        }
                    }
                }
            }
        })
    }

    fun searchContent(keywords: String) {
        //记录关键词
        lastKeyWord = keywords
        //每次搜索音乐时，重置当前所在的页数
        currentPage = 0
        //剩余数量置为0
        remainingCount = 0

        presenter.searchSongs(pageSize, 0, SEARCH_TYPE, keywords)
    }

    override fun searchSuccess(list: MutableList<Song>, songCount: Int) {
        LogUtil.d(TAG, "searchSuccess,songCount:${songCount}")

        //记录剩余歌曲的总数量
        remainingCount = if (songCount - pageSize >= 0) songCount - pageSize else 0
        //当前页数加1
        currentPage++

        adapter.clearAndAddNewDatas(list)
    }

    override fun loadMoreSuccess(list: MutableList<Song>, songCount: Int) {
        LogUtil.d(TAG, "loadMoreSuccess,songCount:${songCount}")

        //加载更多结束
        loadingFinishFlag = true
        //更新剩余歌曲的数量.
        remainingCount = if (remainingCount - pageSize >= 0) remainingCount - pageSize else 0
        //当前页数加1
        currentPage++

        //移除FooterView
        removeFooterView()

        //设置数据
        adapter.addDatas(list)
    }

    override fun searchError(msg: String) {
        Toast.makeText(context, "错误：$msg", Toast.LENGTH_SHORT).show()
    }

    override fun loadMoreError(msg: String) {
        Toast.makeText(context, "错误：$msg", Toast.LENGTH_SHORT).show()
    }

    private fun setFooterView(recyclerView: RecyclerView) {
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.footer_view, recyclerView,
            false
        )
        adapter.setFooterView(view)
    }

    private fun removeFooterView() {
        adapter.removeFooterView()
    }

    override fun setPresenter(presenter: SearchContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
        loadingFl.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingFl.visibility = View.GONE
    }

    /**
     * 该方法在这里暂时没用，上面有两个方法分别对两种不同的error进行了处理.
     * */
    override fun showError(errorMessage: String) {
//        Toast.makeText(context, "错误：$errorMessage", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(v: View?, position: Int) {
        ThreadUtil.runOnThreadPool(Runnable { PlayServiceManager.playSongs(adapter.getItems(),position)})
    }
}
