package com.example.tvmusicplayer.bean

import android.os.Parcel
import android.os.Parcelable

class Song : Parcelable{
    var id: Long? = null
    var url: String? = null
    var size: Long? = null

    /**
     * 歌曲名字.
     * */
    var name: String? = null

    /**
     * 码率.
     * */
    var br: Long? = null

    /**
     * 歌手名字.
     * */
    var artistName: String? = null

    /**
     * 专辑图片的url.
     * */
    var picUrl: String? = null

    constructor(parcel: Parcel){
        id = parcel.readLong()
        url = parcel.readString()
        size = parcel.readLong()
        name = parcel.readString()
        br = parcel.readLong()
        artistName = parcel.readString()
        picUrl = parcel.readString()
    }

    constructor() {
    }

    constructor(
        id: Long?,
        url: String?,
        size: Long?,
        name: String?,
        br: Long?,
        artistName: String?,
        picUrl: String?
    ) {
        this.id = id
        this.url = url
        this.size = size
        this.name = name
        this.br = br
        this.artistName = artistName
        this.picUrl = picUrl
    }

    override fun toString(): String {
        return "id:$id,url:$url,size:$size,name:$name,br:$br,artistName:$artistName,picUrl:$picUrl"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(url)
        parcel.writeValue(size)
        parcel.writeString(name)
        parcel.writeValue(br)
        parcel.writeString(artistName)
        parcel.writeString(picUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}