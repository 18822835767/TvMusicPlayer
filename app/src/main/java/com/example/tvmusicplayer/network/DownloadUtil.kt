package com.example.tvmusicplayer.network

import android.os.Environment
import com.example.tvmusicplayer.MyApplication
import com.example.tvmusicplayer.util.LogUtil
import okhttp3.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile
import java.lang.Exception
import java.util.concurrent.TimeUnit

object DownloadUtil {

    private val TAG = "DownloadUtil"
    
    /**
     * 这里文件返回路径为/storage/emulated/0/Android/data/packagename/files/Music.
     * */
    private val path = MyApplication.app!!.getExternalFilesDir(Environment.DIRECTORY_MUSIC)

    private var client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    fun downloadSong(name: String, url: String, listener: DownloadListener){
        var inputStream: InputStream? = null
        var savedFile: RandomAccessFile? = null
        var file: File? = null
        try {
            //获取要下载的文件字节长度
            val contentLength = getContentLength(url)
            var downloadedLength = 0L
            //如果要下载的文件长度为0，那么返回TYPE_FAILED
            if (contentLength == 0L) {
                listener.onFailed()
                return
            }
            //下载的文件存储位置
            file = File("${path}${url.substring(url.lastIndexOf("/"))}")
            LogUtil.d(TAG,"文件存在吗？${file.exists()}")
            //如果文件已经存在，说明该文件有下载过.
            if (file.exists()) {
                //记录已下载的文件长度
                downloadedLength = file.length()
            }
            //如果文件的字节长度相同，说明之前已经下载成功了
            if (contentLength == downloadedLength) {
                listener.onSuccess()
                return
            }
            //Header里告诉服务器，跳过多少字节后开始下载.
            val request: Request = Request.Builder()
                .url(url)
                .addHeader("RANGE", "bytes=$downloadedLength-")
                .build()
            //使用同步的方式获取response
            val response: Response = client.newCall(request).execute()
            //io流写入文件
            response.body?.let {
                inputStream = it.byteStream()
                savedFile = RandomAccessFile(file, "rwd")
                savedFile!!.seek(downloadedLength)
                //字节数组
                val b = ByteArray(1024)
                var total = 0
                var len = inputStream!!.read(b)
                //这里的io操作也是需要网络的.
                while (len != -1) {
                    total += len
                    savedFile!!.write(b, 0, len)
                    //计算下载的百分比
                    val progress = ((total + downloadedLength) * 100 / contentLength).toInt()
                    listener.onProgress(progress)
                    len = inputStream!!.read(b)
                }
                it.close()
                listener.onSuccess()
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try{
                inputStream?.close()
                savedFile?.close()
                //todo 被取消，文件要删除.    
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
        listener.onFailed()
        return
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