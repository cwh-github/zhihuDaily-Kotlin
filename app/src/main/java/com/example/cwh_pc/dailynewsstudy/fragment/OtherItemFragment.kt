package com.example.cwh_pc.dailynewsstudy.fragment

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.cwh_pc.dailynewsstudy.MainActivity
import com.example.cwh_pc.dailynewsstudy.R
import com.example.cwh_pc.dailynewsstudy.adapter.OtherNewsAdapter
import com.example.cwh_pc.dailynewsstudy.adapter.OtherThemeLoadMoreListener
import com.example.cwh_pc.dailynewsstudy.extension.LogUtils
import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeData
import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeMoreData
import com.example.cwh_pc.dailynewsstudy.model.entities.Story
import com.example.cwh_pc.dailynewsstudy.presenter.OtherThemePresenter
import com.example.cwh_pc.dailynewsstudy.view.OtherThemeView
import kotlinx.android.synthetic.main.fragment_layout.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class OtherItemFragment:BaseFragment(),OtherThemeView {


    private var adapter:OtherNewsAdapter?=null

    var mContext:MainActivity?=null

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        mContext= activity as MainActivity
    }

    override fun onGetOtherThemeMoreData(otherThemeMoreData: OtherThemeMoreData) {
        val storise=otherThemeMoreData.stories
        if(storise!=null){
            adapter!!.stories!!.addAll((storise as ArrayList<OtherThemeData.StoriesBean>))
            adapter!!.notifyDataSetChanged()
        }

    }

    override fun onGetOtherThemeData(otherThemeData: OtherThemeData) {
        LogUtils.d(msg="Story Data is:${otherThemeData.toString()}")
        initThemeView(otherThemeData)
    }


    private fun initThemeView(otherThemeData: OtherThemeData) {
        swipeRefreshLayout.isRefreshing=false
        if(adapter==null){
            adapter = if(otherThemeData.stories!=null){
                OtherNewsAdapter(otherThemeData,(otherThemeData.stories as ArrayList<OtherThemeData.StoriesBean>),
                        otherThemeData.editors,context)
            }else{
                OtherNewsAdapter(otherThemeData,null,
                        otherThemeData.editors,context)
            }
            adapter!!.loadMoreListener=loadMoreListener
            mHomeRecyclerView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            mHomeRecyclerView.addOnScrollListener(scrollerListener)
            mHomeRecyclerView.adapter=adapter
        }else{
            if(otherThemeData.stories!=null){
                adapter!!.stories=(otherThemeData.stories as ArrayList<OtherThemeData.StoriesBean>)
            }else{
                adapter!!.stories=null
            }
            adapter!!.otherThemeData=otherThemeData
            adapter!!.editors=otherThemeData.editors
            adapter!!.notifyDataSetChanged()
            mHomeRecyclerView.scrollToPosition(0)
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


    val loadMoreListener = object :OtherThemeLoadMoreListener{
        override fun loadMore() {
            val size=adapter!!.stories?.size ?: 0
            if(size!=0){
                val id=(adapter!!.stories!![size-1].id).toLong()
                if(id!=0L){
                    presenter.getOtherThemeMoreStory(themeId, id)
                }
            }

        }

    }

    override fun onStartLoad() {
    }

    override fun onFailer(e: Throwable) {
        swipeRefreshLayout.isRefreshing=false
        if(adapter!=null){
            adapter!!.loadFail()
        }
        LogUtils.d(msg="Error Msg is :${e.message}")
    }

    override fun onComplete() {
        swipeRefreshLayout.isRefreshing=false
        if(adapter!=null){
            adapter!!.loadComplete()
        }
    }

    override fun onDestory() {
        presenter.ondestory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreate=false
        isLoadData=false
    }

    var isViewCreate=false
    var isLoadData=false
    var themeId=-1
    lateinit var presenter:OtherThemePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeId=arguments.getInt("id")
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater?.inflate(R.layout.fragment_layout,container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter= OtherThemePresenter(this,this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setOnRefreshListener {
            presenter.getOtherStoryData(themeId)
        }
        isViewCreate=true
        if(!isLoadData && themeId!=-1){
            presenter.getOtherStoryData(themeId)
            swipeRefreshLayout?.isRefreshing=true
            isLoadData=true
        }
    }


    fun onLoadData(id:Int){
        LogUtils.d("Fragment","LoadData")
        themeId=id
        if(isViewCreate){
            swipeRefreshLayout?.isRefreshing=true
            presenter.getOtherStoryData(id)
            isLoadData=true
        }
    }



}