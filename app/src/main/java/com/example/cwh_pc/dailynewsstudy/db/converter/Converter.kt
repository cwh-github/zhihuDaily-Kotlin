package com.example.cwh_pc.dailynewsstudy.db.converter

import android.arch.persistence.room.TypeConverter
import android.text.TextUtils

class Converter{
    @TypeConverter
    fun arrayToString(array:ArrayList<String>?):String?{
        if(array==null || array.isEmpty()){
            return null
        }
        val sb=StringBuffer()
        sb.append(array[0])
        for(i in 1 until array.size){
            sb.append("~").append(array[i])
        }
        return sb.toString()
    }

    @TypeConverter
    fun stringToArray(str:String?):ArrayList<String>?{
        return if(TextUtils.isEmpty(str))
            null
        else {
            val strs=str!!.split("~")
            val arrays=ArrayList<String>()
            for(item in strs){
                arrays.add(item)
            }
            arrays
        }
    }

}