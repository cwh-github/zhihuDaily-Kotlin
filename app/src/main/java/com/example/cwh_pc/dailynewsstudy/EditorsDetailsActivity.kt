package com.example.cwh_pc.dailynewsstudy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.example.cwh_pc.dailynewsstudy.extension.LogUtils
import com.example.cwh_pc.dailynewsstudy.extension.click
import kotlinx.android.synthetic.main.activity_editors_details.*
import kotlinx.android.synthetic.main.activity_story_details.*

class EditorsDetailsActivity : BaseActivity() {
    override fun onDestory() {
        if (mDetailsWebView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            val parent = mDetailsWebView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(mDetailsWebView)
            }
            mDetailsWebView.stopLoading()
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mDetailsWebView.settings.javaScriptEnabled = false
            mDetailsWebView.clearHistory()
            mDetailsWebView.clearView()
            mDetailsWebView.removeAllViews()

            try {
                mDetailsWebView.destroy()
            } catch (ex: Throwable) {

            }

        }
    }

    var url:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editors_details)
        url=intent.getStringExtra("url")
        LogUtils.d(msg="url is $url")
        initView()
    }

    private fun initView() {
        imageDetailsBack.click {
            finish()
        }
        mDetailsWebView.settings.javaScriptEnabled=true
        mDetailsWebView.loadUrl(url)
    }
}
