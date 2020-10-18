package com.example.tvmusicplayer.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.collections.forEach as forEach1

/**
 * 运行时权限的请求.
 * */
class PermissionHelper {
    companion object {

        fun requestPermissions(activity : Activity, permissions: Array<String>, requestCode : Int){
            val permissionList = mutableListOf<String>()
            
            //筛选出需要处理的权限
            permissions.forEach1 { 
                if(ContextCompat.checkSelfPermission(activity,it) != PackageManager.PERMISSION_GRANTED){
                    permissionList.add(it)
                }
            }
            
            //权限请求
            if(permissionList.isNotEmpty()){
                ActivityCompat.requestPermissions(activity,permissionList.toTypedArray(),requestCode)
            }
        }
        
        /**
         * 判断权限有没有全部申请成功
         */
        fun permissionAllow(context: Context, permissions: Array<String>): Boolean {
            for (i in permissions.indices) {
                if (ContextCompat.checkSelfPermission(context, permissions[i]) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }
    }
}