package com.example.cwh_pc.dailynewsstudy.extension

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.jetbrains.anko.connectivityManager
import android.Manifest.permission.INTERNET
import android.support.annotation.RequiresPermission
import android.util.Log
import com.example.cwh_pc.dailynewsstudy.MyApplication


object NetWorkUtils {

    /**
     * is Wifi Connect
     */
     fun isWifiConnect(context: Context): Boolean {
        val conm = context.connectivityManager
        return conm!=null && conm!!.activeNetworkInfo!=null &&
                conm.activeNetworkInfo.type==ConnectivityManager.TYPE_WIFI
    }

    private fun getNetWorkInfo(context: Context):NetworkInfo?{
        val conm = context.connectivityManager
        return conm.activeNetworkInfo
    }

    fun isMobileData(context: Context): Boolean {
        val info = getNetWorkInfo(context)
        return (null != info
                && info!!.isAvailable
                && info!!.type == ConnectivityManager.TYPE_MOBILE)
    }

    fun isConnectNet():Boolean{
        return isWifiConnect(MyApplication.application!!) ||
                isMobileData(MyApplication.application!!)
    }

}