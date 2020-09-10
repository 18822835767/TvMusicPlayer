package com.example.tvmusicplayer.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.adapter.UserAdapter
import com.example.tvmusicplayer.util.Constant

class UserFragment : Fragment() {
    
    private lateinit var mRecyclerView : RecyclerView
    
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String): UserFragment {
            return UserFragment()
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView = view.findViewById(R.id.recycler_view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        val userText = mutableListOf<String>()
        userText.add(Constant.LOCAL_MUSIC)
        userText.add(Constant.MY_SONG_LIST)
        userText.add(Constant.DOWNLOAD_MANAGER)
        val adapter : UserAdapter = UserAdapter(userText,R.layout.user_item)
        mRecyclerView.adapter = adapter
        val manager = LinearLayoutManager(context)
        mRecyclerView.layoutManager = manager
    }
}
