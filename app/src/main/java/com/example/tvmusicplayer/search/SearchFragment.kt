package com.example.tvmusicplayer.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.adapter.SearchAdapter
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.bean.Song

class SearchFragment : Fragment(),SearchContract.OnView,BaseRecyclerViewAdapter.OnItemClickListener {

    private lateinit var loadingFl : FrameLayout
    private lateinit var presenter : SearchContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var manager : LinearLayoutManager
    
    companion object {
        fun newInstance() : SearchFragment{
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
    }
    
    private fun initData(){
        //构造presenter
        SearchPresenter(this)
        
        adapter = SearchAdapter(mutableListOf<Song>(),R.layout.song_item)
        adapter.setItemClickListener(this)
        manager = LinearLayoutManager(context)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        //添加分割线
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun searchSuccess(list: MutableList<Song>) {
        adapter.addDatas(list)
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

    override fun showError(errorMessage: String) {
        Toast.makeText(context,"错误：$errorMessage", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(v: View?, position: Int) {
        TODO("Not yet implemented")
    }
}
