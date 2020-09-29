package com.example.tvmusicplayer.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.bean.Song

class SearchFragment : Fragment(),SearchContract.OnView {

    private lateinit var loadingFl : FrameLayout
    private lateinit var presenter : SearchContract.Presenter
    
    companion object {
        fun newInstance() : SearchFragment{
            return SearchFragment()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }
    
    private fun initData(){
        //构造presenter
        SearchPresenter(this)
    }

    override fun searchSuccess(list: MutableList<Song>) {
        TODO("Not yet implemented")
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
}
