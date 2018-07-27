package com.example.cwh_pc.dailynewsstudy.extension

import android.content.Context
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.cwh_pc.dailynewsstudy.GlideApp
import com.example.cwh_pc.dailynewsstudy.R
import java.io.*
import java.security.MessageDigest
import kotlin.experimental.and


fun loadImage(context: Context, imageView:ImageView,url:String?,
              @DrawableRes errorid:Int=R.drawable.load_image_erroe,
              @DrawableRes placeholderId:Int=R.drawable.load_image_erroe){
    GlideApp.with(context)
            .load(url)
            .error(errorid)
            .placeholder(placeholderId)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .skipMemoryCache(false)
            .centerCrop()
            .into(imageView)
}

fun loadCircleImage(context: Context,imageView: ImageView,url: String?, @DrawableRes errorid:Int=R.drawable.load_image_erroe,
                    @DrawableRes placeholderId:Int=R.drawable.load_image_erroe){
    GlideApp.with(context)
            .load(url)
            .error(errorid)
            .placeholder(placeholderId)
            .centerCrop()
            .apply(bitmapTransform(CircleCrop()))
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .skipMemoryCache(false)
            .into(imageView)

}

/**
 * call it on Async Thread
 */
fun downloadImageAsFile(context: Context,url:String):String{
   val target=GlideApp.with(context)
            .asFile()
            .load(url)
            .submit()
    var cache:File
    cache=context.externalCacheDir
    if(cache==null){
        cache=context.cacheDir
    }
    val fileName=string2MD5(url)
    var file:File
    file = File(cache,fileName)
    if(!file.exists()){
        file.createNewFile()
    }
    val targetFile=target.get()
    var inputStream:FileInputStream?=null
    var outputStream:FileOutputStream?=null
    try {
        inputStream=FileInputStream(targetFile)
        outputStream=FileOutputStream(file)
        val b = ByteArray(1024 )
        var len=0
        while(true){
            len=inputStream.read(b)
            if(len==-1){
                break
            }
            outputStream.write(b,0,len)
        }
        outputStream.flush()
    }catch (e:FileNotFoundException){
        e.printStackTrace()
        LogUtils.d(msg="File Not Found message :${e.message}")
        return " "
    }catch (e:Exception){
        e.printStackTrace()
        LogUtils.d(msg="Exception is :${e.message}")
        return " "
    }finally {
        outputStream?.close()
        inputStream?.close()
    }
    return file.absolutePath
}

private fun string2MD5(url:String):String{
    val stringBuilder = StringBuilder()
    try {
        val md5 = MessageDigest.getInstance("MD5")
        md5.update(url.toByteArray())
        val bytes = md5.digest()
        for (i in bytes.indices) {
            if (bytes[i] and 0xff.toByte() < 0x10) {
                stringBuilder.append("0")
            }
            stringBuilder.append(java.lang.Long.toString((bytes[i] and 0xff.toByte()).toLong(), 16))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return stringBuilder.toString()
}


