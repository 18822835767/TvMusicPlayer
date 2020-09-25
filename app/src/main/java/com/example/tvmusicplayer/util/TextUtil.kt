package com.example.tvmusicplayer.util

import java.text.SimpleDateFormat
import java.util.*

class TextUtil {
    companion object {
        /**
         * 将传入的时间转化为"mm:ss"格式.
         * */
        fun getTimeStr(duration: Long): String {
            val date = Date(duration)
            val timeFormat = SimpleDateFormat("mm:ss")
            return timeFormat.format(date)
        }
    }
}