package com.example.cwh_pc.dailynewsstudy.fragment

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.cwh_pc.dailynewsstudy.MainActivity
import com.example.cwh_pc.dailynewsstudy.R
import com.example.cwh_pc.dailynewsstudy.adapter.HomeNewsAdapter
import com.example.cwh_pc.dailynewsstudy.adapter.LoadMoreListener
import com.example.cwh_pc.dailynewsstudy.extension.LogUtils
import com.example.cwh_pc.dailynewsstudy.extension.NetWorkUtils
import com.example.cwh_pc.dailynewsstudy.extension.toast
import com.example.cwh_pc.dailynewsstudy.model.entities.BeforeNewsData
import com.example.cwh_pc.dailynewsstudy.model.entities.LatestNewsData
import com.example.cwh_pc.dailynewsstudy.model.entities.MenuData
import com.example.cwh_pc.dailynewsstudy.model.entities.Story
import com.example.cwh_pc.dailynewsstudy.presenter.HomePresenter
import com.example.cwh_pc.dailynewsstudy.presenter.StoryDBPresenter
import com.example.cwh_pc.dailynewsstudy.view.HomeView
import kotlinx.android.synthetic.main.fragment_layout.*

class HomeFragment:BaseFragment(),HomeView{


    override fun onBeforNewsData(beforeNewsData: BeforeNewsData) {
        val stories=dealNewsData(beforeNewsData.stories as ArrayList<Story>,beforeNewsData.date)
        if(adapter!=null && stories!=null){
            adapter!!.newsData!!.addAll(stories!!)
            adapter!!.notifyDataSetChanged()
        }

    }

    var adapter:HomeNewsAdapter?=null
    var layoutManager:LinearLayoutManager?=null
    var mContext:MainActivity?=null
    override fun onDestory() {
        homePresenter.ondestory()
    }

    override fun onMenuData(menuData: MenuData) {
    }

    override fun onLatestNewsData(latestNewsData: LatestNewsData) {
        LogUtils.d(msg="Latest Data: $latestNewsData")
        swipeRefreshLayout.isRefreshing=false
        val stories=dealNewsData(latestNewsData.stories!!,latestNewsData.date!!)
        if(adapter==null){
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            mHomeRecyclerView.layoutManager=layoutManager
            adapter=HomeNewsAdapter(stories,latestNewsData.top_stories,context)
            adapter!!.loadMoreListener=loadMooreListener
            mHomeRecyclerView.addOnScrollListener(scrollerListener)
            mHomeRecyclerView.adapter=adapter
        }else{
            adapter!!.newsData=stories
            adapter!!.topNews=latestNewsData.top_stories
            adapter!!.notifyDataSetChanged()
        }

//        if(latestNewsData.stories!=null){
//            LogUtils.d(msg="Last story is: ${latestNewsData.stories.toString()}")
//            dbPresenter.insertOrUpDateStory(latestNewsData.date ?: 0L,latestNewsData.stories!!)
//        }

    }

    private val loadMooreListener=object :LoadMoreListener{
        override fun loadingMore() {
            LogUtils.d(msg="Load More")
            val size=adapter!!.newsData?.size ?: 0
            val date=adapter!!.newsData?.get(size-1)?.date
            if(NetWorkUtils.isConnectNet()){
                homePresenter.getBeforNewsData(date)
            }else{
                homePresenter.getBeforeNewsDataOnNoNet(date)
            }

        }

    }

    val scrollerListener=object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when(newState){
                RecyclerView.SCROLL_STATE_IDLE,
                RecyclerView.SCROLL_STATE_DRAGGING->{
                    if (mContext != null && !mContext!!.isFinishing &&
                            Glide.with(mContext!!).isPaused) {
                        Glide.with(mContext!!).resumeRequests()
                    }

                    val position=layoutManager?.findFirstVisibleItemPosition() ?:0
                    if(mContext!=null){
                        if(position==0){
                            mContext!!.notityToolbarTitle("首页")
                        }else {
                            val dateTitle= adapter?.getDateTitle(position) ?: ""
                            mContext!!.notityToolbarTitle(dateTitle)
                        }
                    }
                }

                RecyclerView.SCROLL_STATE_SETTLING->{
                    if (mContext != null && !mContext!!.isFinishing &&
                            !Glide.with(mContext!!).isPaused) {
                        Glide.with(mContext!!).pauseRequests()
                    }
                }
            }

        }
    }

    //对获取的新闻信息进行处理
    private fun dealNewsData(stories:ArrayList<Story>,date:Long):ArrayList<Story>? {
        val newStories:ArrayList<Story> = ArrayList()
        stories.forEach {
            newStories.add(it)
        }
        if (newStories != null) {
            for(story in newStories){
                story.date=date
            }
            val newStory=Story(-1L)
            newStory.date=date
            newStories.add(0,newStory)
        }
        return newStories
    }

    override fun onStartLoad() {
        //swipeRefreshLayout.isRefreshing=true
    }

    override fun onFailer(e: Throwable) {
        swipeRefreshLayout.isRefreshing=false
        if(adapter!=null){
            adapter!!.loadFail()
            LogUtils.d(msg="load Fail Msg is: ${e.message}")
        }
    }

    override fun onComplete() {
        swipeRefreshLayout.isRefreshing=false
        if(adapter!=null){
            adapter!!.loadComplete()
        }
    }

    lateinit var homePresenter: HomePresenter
    lateinit var dbPresenter: StoryDBPresenter


    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        mContext=activity as MainActivity
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_layout,container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbPresenter= StoryDBPresenter(context,this)
        homePresenter= HomePresenter(this,this)
        if(!NetWorkUtils.isConnectNet()){
            homePresenter.getLatestNewsDataOnNoNet()
        }else{
            homePresenter.getLatestNewsData()
        }
        swipeRefreshLayout.isRefreshing=true
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        swipeRefreshLayout.setOnRefreshListener {
            if(NetWorkUtils.isConnectNet()){
                homePresenter.getLatestNewsData()
            }else{
                homePresenter.getLatestNewsDataOnNoNet()
//                swipeRefreshLayout.isRefreshing=false
//                context.toast("暂无网络")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter?.ondestory()
        dbPresenter?.ondestory()
    }

}