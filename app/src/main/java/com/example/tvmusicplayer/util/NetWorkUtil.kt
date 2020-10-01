package com.example.tvmusicplayer.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * 与网络相关的工具类.
 * */
class NetWorkUtil {
    companion object {

        /**
         * 判断当前是否有网络连接.
         * */
        fun isNetWorkConnected(context: Context): Boolean {
            //获取手机所以管理连接的对象(包括wifi、net等连接的管理)
            val manager: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE)
                        as ConnectivityManager
            //获取NetworkInfo对象
            val networkInfo: NetworkInfo? = manager.activeNetworkInfo
            networkInfo?.let {
                return networkInfo.isAvailable
            }
            return false
        }
    }
}