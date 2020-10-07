package com.example.tvmusicplayer.network

import android.os.Environment
import com.example.tvmusicplayer.MyApplication
import com.example.tvmusicplayer.util.Constant.DownloadSong.TYPE_FAILED
import com.example.tvmusicplayer.util.Constant.DownloadSong.TYPE_SUCCESS
import okhttp3.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile
import java.lang.Exception
import java.util.concurrent.TimeUnit

object DownloadUtil {

    /**
     * 这里文件返回路径为/storage/emulated/0/Android/data/packagename/files/Music.
     * */
    private val path = MyApplication.app!!.getExternalFilesDir(Environment.DIRECTORY_MUSIC)

    private var client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    fun downloadSong(name: String, url: String, listener: DownloadListener): Int {
//        var inputStream : InputStream? = null
//        var savedFile : RandomAccessFile? = null
//        var file : File? = null
//        try {
//            
//        }catch (e : Exception){
//            
//        }finally {
//            
//        }
        
        val contentLength = getContentLength(url)
        var downloadedLength = 0L
        //如果要下载的文件长度为0，那么返回TYPE_FAILED
        if (contentLength == 0L) {
            return TYPE_FAILED
        }
        val file = File("${path}${url.substring(url.lastIndexOf("/"))}")
        //如果文件已经存在，说明该文件有下载过.
        if (file.exists()) {
            //记录已下载的文件长度
            downloadedLength = file.length()
        }

        //如果文件的字节长度相同，说明之前已经下载成功了
        if (contentLength == downloadedLength) {
            return TYPE_SUCCESS
        }
        //Header里告诉服务器，跳过多少字节后开始下载.
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("RANGE", "bytes=$downloadedLength-")
            .build()
        //使用同步的方式获取response
        val response : Response = client.newCall(request).execute()
        response.body?.let { 
            val inputStream : InputStream = it.byteStream()
            val savedFile : RandomAccessFile = RandomAccessFile(file,"rwd")
            savedFile.seek(downloadedLength)
            //字节数组
            val b = ByteArray(1024)
            var total = 0
            var len = inputStream.read(b)
            while (len != -1){
                total += len
                savedFile.write(b,0,len)
                //计算下载的百分比
                val progress = ((total + downloadedLength) * 100 / contentLength).toInt()
                listener.onProgress(progress)
                len = inputStream.read(b)
            }
            it.close()
            return TYPE_SUCCESS
        }

        return TYPE_FAILED

    }

    /**
     * 传入要下载url，获取文件的长度(字节).
     * 这里是采用同步请求获取.
     * */
    private fun getContentLength(downloadUrl: String): Long {
        val request: Request = Request.Builder()
            .url(downloadUrl)
            .build()
        val response: Response = client.newCall(request).execute()
        if (response.isSuccessful) {
            response.body?.let {
                val contentLength = it.contentLength()
                it.close()
                return contentLength
            }
        }
        return 0
    }
}