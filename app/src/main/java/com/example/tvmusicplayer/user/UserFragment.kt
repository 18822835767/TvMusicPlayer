package com.example.tvmusicplayer.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.tvmusicplayer.R
import com.example.tvmusicplayer.adapter.UserAdapter
import com.example.tvmusicplayer.base.BaseRecyclerViewAdapter
import com.example.tvmusicplayer.playlist.UserPlayListActivity
import com.example.tvmusicplayer.util.Constant
import com.example.tvmusicplayer.manager.LoginStatusManager

class UserFragment : Fragment(),BaseRecyclerViewAdapter.OnItemClickListener{
    
    private lateinit var mRecyclerView : RecyclerView
    private lateinit var adapter : UserAdapter
    
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
        adapter = UserAdapter(userText,R.layout.user_item)
        adapter.setItemClickListener(this)
        mRecyclerView.adapter = adapter
        val manager = LinearLayoutManager(context)
        mRecyclerView.layoutManager = manager
    }

    override fun onItemClick(v: View?, position: Int) {
        when(adapter.getItem(position)){
            //如果用户点击的是"我的歌单"
            Constant.MY_SONG_LIST ->{
                //若已经登陆
                if(LoginStatusManager.alreadyLogin){
                    context?.let { UserPlayListActivity.actionStart(it) }
                }else{
                  //还没登陆
                    Toast.makeText(context,"亲，请先登陆噢~",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
