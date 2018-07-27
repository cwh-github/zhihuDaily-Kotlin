package com.example.cwh_pc.dailynewsstudy.extension

import android.util.Log

class LogUtils {

    private constructor()

    companion object {
        val isDebug=true
        fun d(tag:String="DailyNews",msg:String){
            if(isDebug)
                Log.d(tag,msg)
        }

        fun v(tag:String="DailyNews",msg:String){
            if(isDebug)
                Log.v(tag,msg)
        }

        fun i(tag:String="DailyNews",msg:String){
            if(isDebug)
                Log.i(tag,msg)
        }

        fun e(tag:String="DailyNews",msg:String){
            if(isDebug)
                Log.e(tag,msg)
        }

        fun w(tag:String="DailyNews",msg:String){
            if(isDebug)
                Log.w(tag,msg)
        }
    }


}