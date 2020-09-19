package com.example.tvmusicplayer.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * 歌单的实体类.
 * */
class PlayList : Parcelable {
    var id: Long? = null
    var name: String? = null
    var coverImgUrl: String? = null

    constructor() {}

    constructor(id: Long?, name: String?, coverImgUrl: String?) {
        this.id = id
        this.name = name
        this.coverImgUrl = coverImgUrl
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
        coverImgUrl = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id ?: -1)
        dest.writeString(name)
        dest.writeString(coverImgUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayList> {
        override fun createFromParcel(parcel: Parcel): PlayList {
            return PlayList(parcel)
        }

        override fun newArray(size: Int): Array<PlayList?> {
            return arrayOfNulls(size)
        }
    }
}