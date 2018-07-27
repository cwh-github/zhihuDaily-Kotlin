package com.example.cwh_pc.dailynewsstudy

import android.support.v7.app.AppCompatActivity


abstract class BaseActivity:AppCompatActivity() {

   abstract fun onDestory()


    override fun onDestroy() {
        super.onDestroy()
        onDestory()
    }
}