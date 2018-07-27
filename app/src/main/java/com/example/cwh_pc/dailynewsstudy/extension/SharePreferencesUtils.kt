package com.example.cwh_pc.dailynewsstudy.extension

import android.content.Context
import android.content.SharedPreferences

object SharePreferencesUtils{
    private const val THEME_SP="theme_is_night"
    private const val DELETE_DATE_SP="delete_date"

    fun setIsNightTheme(context: Context,isNightTheme:Boolean){
        val sp=context.getSharedPreferences(THEME_SP,Context.MODE_PRIVATE)
        sp.edit().putBoolean("theme",isNightTheme).commit()
    }

    fun getIsNightTheme(context: Context):Boolean{
        val sp=context.getSharedPreferences(THEME_SP,Context.MODE_PRIVATE)
        return sp.getBoolean("theme",false)
    }

    fun saveDeleteDate(context: Context){
        val sp=context.getSharedPreferences(DELETE_DATE_SP,Context.MODE_PRIVATE)
        sp.edit().putLong("date",System.currentTimeMillis()).commit()
    }

    fun isNeedCheckDelete(context: Context):Boolean{
        val sp=context.getSharedPreferences(DELETE_DATE_SP,Context.MODE_PRIVATE)
        val lastDate=sp.getLong("date",0L)
        return System.currentTimeMillis()-lastDate>=12*60*60*1000
    }
}