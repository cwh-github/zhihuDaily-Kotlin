package com.example.cwh_pc.dailynewsstudy

import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.cwh_pc.dailynewsstudy.common.Constants
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    val handler =object :Handler(){

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg!!.what){
                START_MAIN->{
                    startActivity<MainActivity>()
                    finish()
                }
            }
        }

    }
    private val START_MAIN: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigation()
        setContentView(R.layout.activity_splash)
        handler.sendEmptyMessageDelayed(START_MAIN,1500)
        loadSplashImage()
    }

    private fun loadSplashImage() {
        GlideApp.with(this)
                .load(Constants.SPLASH_URL)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.mipmap.splash)
                .centerCrop()
                .listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }


                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        if(resource!=null){
                            mImageSplash.setImageDrawable(resource)
                            handler.removeCallbacksAndMessages(null)
                            handler.sendEmptyMessageDelayed(START_MAIN,1500)
                            return true
                        }
                        return false
                    }

                })
                .error(R.mipmap.splash)
                .into(mImageSplash)


    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }

    fun hideNavigation(){
        var system=window.decorView.systemUiVisibility
        if(Build.VERSION.SDK_INT>=14){
            system=system xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        window.decorView.systemUiVisibility=system
    }


}
