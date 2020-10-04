package com.example.tvmusicplayer.bean

import android.os.Parcel
import android.os.Parcelable

class Song : Parcelable, Comparable<Song> {
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

    /**
     * 歌曲名字的第一个字母.
     * */
    var firstLetter: Char = ' '

    /**
     * 标记是否在在线歌曲，默认为是.
     * */
    var online = true

    constructor(parcel: Parcel) {
        id = parcel.readLong()
        url = parcel.readString()
        size = parcel.readLong()
        name = parcel.readString()
        br = parcel.readLong()
        artistName = parcel.readString()
        picUrl = parcel.readString()
        firstLetter = parcel.readInt().toChar()
        online = (parcel.readByte().toInt() != 0)
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
        parcel.writeLong(id ?: -1)
        parcel.writeString(url)
        parcel.writeLong(size ?: -1)
        parcel.writeString(name)
        parcel.writeLong(br ?: -1)
        parcel.writeString(artistName)
        parcel.writeString(picUrl)
        parcel.writeInt(firstLetter.toInt())
        parcel.writeByte(if(online) 1 else 0)
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

    override fun compareTo(other: Song): Int {
        if (this.firstLetter == other.firstLetter) {
            return 0
        } else if (other.firstLetter == '#') {
            return -1
        } else if (this.firstLetter == '#') {
            return 1
        }

        return this.firstLetter.compareTo(other.firstLetter);
    }
}