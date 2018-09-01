package com.example.cwh_pc.dailynewsstudy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.cwh_pc.dailynewsstudy.adapter.MenuItemAdapter
import com.example.cwh_pc.dailynewsstudy.adapter.MenuItemClickListener
import com.example.cwh_pc.dailynewsstudy.download.OffLineDownloadIntentService
import com.example.cwh_pc.dailynewsstudy.extension.LogUtils
import com.example.cwh_pc.dailynewsstudy.extension.SharePreferencesUtils
import com.example.cwh_pc.dailynewsstudy.extension.toast
import com.example.cwh_pc.dailynewsstudy.fragment.HomeFragment
import com.example.cwh_pc.dailynewsstudy.fragment.OtherItemFragment
import com.example.cwh_pc.dailynewsstudy.model.entities.BeforeNewsData
import com.example.cwh_pc.dailynewsstudy.model.entities.LatestNewsData
import com.example.cwh_pc.dailynewsstudy.model.entities.MenuData
import com.example.cwh_pc.dailynewsstudy.presenter.HomePresenter
import com.example.cwh_pc.dailynewsstudy.presenter.StoryDBPresenter
import com.example.cwh_pc.dailynewsstudy.view.HomeView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tool_bar_main.*
import android.app.ActivityManager
import org.jetbrains.anko.startActivity


class MainActivity : BaseActivity(), HomeView {


    override fun onBeforNewsData(beforeNewsData: BeforeNewsData) {
    }

    val HOME_ID = 0
    override fun onLatestNewsData(latestNewsData: LatestNewsData) {
    }

    override fun onDestory() {
        homePresenter.ondestory()
        dbPresenter.ondestory()
        if (reciver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(reciver)
        }
    }

    override fun onMenuData(menuData: MenuData) {
        initMenu(menuData)
    }

    override fun onStartLoad() {
    }

    override fun onFailer(e: Throwable) {
        initMenu(null)
    }

    override fun onComplete() {
    }

    lateinit var homePresenter: HomePresenter
    lateinit var homeFragment: HomeFragment
    lateinit var otherItemFragment: OtherItemFragment
    var currentId: Int = HOME_ID
    lateinit var dbPresenter: StoryDBPresenter
    var reciver: BroadcastReceiver? = null
    var isDownloading = false
    var downText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver()
        isDownloading = isServiceWorked("com.example.cwh_pc.dailynewsstudy.download.OffLineDownloadIntentService")
        homePresenter = HomePresenter(this, this)
        dbPresenter = StoryDBPresenter(this, this)
        initView()
    }

    override fun onStart() {
        super.onStart()
        if (SharePreferencesUtils.isNeedCheckDelete(this)) {
            dbPresenter.deleteStoryByLessNDay(10)
        }
    }

    private fun registerReceiver() {
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        val filter = IntentFilter()
        filter.addAction("com.download.error")
        filter.addAction("com.download.progress")
        reciver = MyBroadCastReciver()
        localBroadcastManager.registerReceiver(reciver, filter)
    }

    inner class MyBroadCastReciver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val actionStr = intent!!.action
            if (actionStr == "com.download.progress") {
                val progress = intent.getIntExtra("progress", 0)
                if (progress == 100) {
                    downText?.text = "下载完成"
                    isDownloading = false
                } else {
                    isDownloading = true
                    downText?.text = "$progress%"
                }
            } else if (actionStr == "com.download.error") {
                downText?.text = "下载失败"
                isDownloading = false
            }
        }

    }

    private fun isServiceWorked(serviceName: String): Boolean {
        val myManager = this.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningService = myManager.getRunningServices(200) as ArrayList<ActivityManager.RunningServiceInfo>
        for (i in 0 until runningService.size) {
            LogUtils.d(msg = "service name is:${runningService[i].service.className}")
            if (runningService[i].service.className.toString() == serviceName) {
                return true
            }
        }
        return false
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        toolbar.title = "首页"
        toolbar.setTitleTextColor(Color.WHITE)
        val toggle = ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_menu,
                R.string.close_menu)
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        homePresenter.getMenData()
        initMainView()
    }

    fun notityToolbarTitle(title: String) {
        toolbar.title = title
        toolbar.translationY
    }

    /**
     * 初始化右边主要显示内容区域
     */
    private fun initMainView() {
        homeFragment = HomeFragment()
        otherItemFragment = OtherItemFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mFrame, homeFragment, homeFragment::javaClass.name)
        transaction.commit()
    }


    private fun changeNewsView(clickId: Int) {
        if (clickId == currentId) {
            return
        }
        val transaction = supportFragmentManager.beginTransaction()
        if (clickId == HOME_ID && otherItemFragment.isAdded) {
            transaction.hide(otherItemFragment)
            LogUtils.d(msg = "Come in")
            if (homeFragment.isAdded) {
                LogUtils.d(msg = "Come in")
                transaction.show(homeFragment)
            } else {
                transaction.add(R.id.mFrame, homeFragment, homeFragment::javaClass.name)
                        .show(homeFragment)
            }

        } else {
            LogUtils.d(msg = "Come in")
            if (otherItemFragment.isAdded) {
                transaction.hide(homeFragment).show(otherItemFragment)
            } else {
                LogUtils.d(msg = "Come in")
                val bundle = Bundle()
                bundle.putInt("id", clickId)
                otherItemFragment.arguments = bundle
                transaction.hide(homeFragment).add(R.id.mFrame, otherItemFragment, otherItemFragment::javaClass.name).show(otherItemFragment)
            }
        }
        transaction.commit()
        currentId = clickId
    }

    private fun initMenu(menuData: MenuData?) {
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = MenuItemAdapter(menuData?.othersMenu)
        downText = adapter.downTextView
        adapter.menuClickListener = object : MenuItemClickListener {
            override fun onHomeMenuClick() {
                mDrawerLayout.closeDrawer(Gravity.START)
                if (currentId == HOME_ID) {
                    return
                }
                changeNewsView(HOME_ID)
            }

            override fun onItemClick(id: Int, name: String) {
                //toast("Menu Item Click")
                mDrawerLayout.closeDrawer(Gravity.START)
                if (id != currentId) {
                    notityToolbarTitle(name)
                    changeNewsView(id)
                    otherItemFragment.onLoadData(id)
                }

            }

            override fun onCollectionClick() {
                startActivity<CollectActivity>()
            }

            override fun onDownLoadClick(it: View) {
                if (!isDownloading) {
                    //toast("DownLoad Click")
                    val textView = it as TextView
                    downText = textView
                    textView.text = "开始下载"
                    isDownloading = true
                    OffLineDownloadIntentService.startActionDownload(this@MainActivity)
                }
            }

            override fun onOtherClick(view: View) {
                if (view is ImageView) {
                    toast("User Image Click")
                } else {
                    toast("Name Click")
                }
            }

        }
        mRecyclerView.adapter = adapter
    }

    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START)
        } else {
            moveTaskToBack(true)
        }
    }


//    fun TestLifeCycle(){
//        Flowable.interval(3,3,TimeUnit.SECONDS)
//                .doOnCancel {
//                    toast("Test Cancel")
//                }
//                .doOnNext {
//                    LogUtils.d(msg="Long is $it")
//                }
//                .bindUntilEvent(this@MainActivity, Lifecycle.Event.ON_DESTROY)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    toast(msg="Subscriber")
//                    testText.text="Times $it"
//                }
//
//    }

}
