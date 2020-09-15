package com.example.tvmusicplayer.bean

import android.os.Parcel
import android.os.Parcelable


/**
 * 用户的实体类.
 */
class User : Parcelable {
    /**
     * 用户id
     */
    var id: Long = -1
        private set

    /**
     * 用户别名
     */
    var nickName: String? = null

    constructor() {}
    constructor(id: Long, nickName: String?) {
        this.id = id
        this.nickName = nickName
    }

    protected constructor(`in`: Parcel) {
        id = `in`.readLong()
        nickName = `in`.readString()
    }

    fun setId(id: Int) {
        this.id = id.toLong()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(nickName)
    }

    companion object {
        val CREATOR: Parcelable.Creator<User?> = object : Parcelable.Creator<User?> {
            override fun createFromParcel(`in`: Parcel): User? {
                return User(`in`)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }
}
