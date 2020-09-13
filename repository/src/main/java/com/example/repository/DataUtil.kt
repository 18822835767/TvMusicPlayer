package com.example.repository

class DataUtil private constructor() {

    /**
     * 获取单例.
     * */
    fun getInstance(): DataUtil {
        return InstanceHolder.instance
    }

    /**
     * 单例模式，静态内部类构造对象.
     * */
    private class InstanceHolder {
        companion object {
            internal val instance: DataUtil = DataUtil()
        }
    }

}