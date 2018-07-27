package com.example.cwh_pc.dailynewsstudy.extension

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast



val handler: Handler =Handler(Looper.getMainLooper())

var toast:Toast?=null

fun Context.toast(msg:String,duration:Int=Toast.LENGTH_SHORT){
    handler.post {
        cancel()
        toast= Toast.makeText(this.applicationContext,msg,duration)
        toast!!.show()

    }
}


fun cancel(){
    if(toast!=null){
        toast!!.cancel()
        toast=null
    }
}