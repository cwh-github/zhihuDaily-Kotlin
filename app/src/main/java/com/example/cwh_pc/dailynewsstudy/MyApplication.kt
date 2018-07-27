package com.example.cwh_pc.dailynewsstudy

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import java.util.concurrent.CopyOnWriteArrayList

class MyApplication:Application(){

    companion object {
        var application:Context?=null
    }


    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        application=applicationContext
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

}