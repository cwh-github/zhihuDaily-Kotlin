package com.example.cwh_pc.dailynewsstudy.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.text.format.DateUtils
import com.example.cwh_pc.dailynewsstudy.MyApplication
import com.example.cwh_pc.dailynewsstudy.db.AppDataBaseHelper
import com.example.cwh_pc.dailynewsstudy.extension.LogUtils
import com.example.cwh_pc.dailynewsstudy.extension.getLastDay
import com.example.cwh_pc.dailynewsstudy.model.HomeModel
import com.example.cwh_pc.dailynewsstudy.model.entities.*
import com.example.cwh_pc.dailynewsstudy.view.HomeView
import com.example.cwh_pc.dailynewsstudy.view.IBaseView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class HomePresenter(private var homeView: HomeView?, owner: LifecycleOwner) :
        IBasePresenter<IBaseView, LifecycleOwner>(homeView, owner) {
    private val homeModel = HomeModel()

    /**
     * 获取侧滑菜单栏数据
     */
    fun getMenData() {
        homeModel.getMenuData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindUntilEvent(owner!!, Lifecycle.Event.ON_DESTROY)
                .subscribe(object : BaseObserver<MenuData, HomeView>(homeView) {
                    override fun onNext(t: MenuData) {
                        homeView?.onMenuData(menuData = t)
                    }
                })

    }

    /**
     * 获取最近日报信息
     */
    fun getLatestNewsData() {
        homeModel.getLatestNewsData()
                .subscribeOn(Schedulers.io())
                .bindUntilEvent(owner!!, Lifecycle.Event.ON_DESTROY)
                .doOnNext {
                    if (it?.stories != null) {
                        val helper = AppDataBaseHelper.newInstance(MyApplication.application!!)
                        helper.insertOrUpDateStory(it.date ?: 0, it.stories!!)
                        if (it.date != 0L && it.date!! > -1L) {
                            it.stories = helper.getStoryByDate(it!!.date!!) as ArrayList<Story>
                        }
                        if (it?.top_stories != null) {
                            helper.insertTopStory(it.top_stories!!)
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<LatestNewsData, HomeView>(homeView) {
                    override fun onNext(t: LatestNewsData) {
                        homeView?.onLatestNewsData(latestNewsData = t)
                        LogUtils.d(msg = "latest Data is: ${t.stories.toString()}")
                    }

                })
    }


    /**
     * 在无网络的环境下获取缓存数据
     */
    fun getLatestNewsDataOnNoNet() {
        Observable.create(ObservableOnSubscribe<LatestNewsData> {
            val helper = AppDataBaseHelper.newInstance(MyApplication.application!!)
            val latestStory = helper.getLatestStory()
            val topStory = helper.getAllTopStory()
            if (latestStory != null && latestStory.isNotEmpty()) {
                val date = latestStory[0].date
                val latestNewsData = LatestNewsData(date, latestStory as ArrayList<Story>, topStory as ArrayList<TopStory>)
                it.onNext(latestNewsData)
                it.onComplete()
            } else {
                it.onError(Throwable("No Data in SQL"))
            }

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError{
                    homeView?.onFailer(it)
                }
                .bindUntilEvent(owner!!, Lifecycle.Event.ON_DESTROY)
                .subscribe(object : BaseObserver<LatestNewsData, HomeView>(homeView) {
                    override fun onNext(t: LatestNewsData) {
                        homeView?.onLatestNewsData(latestNewsData = t)
                        LogUtils.d(msg = "latest Data is: ${t.stories.toString()}")
                    }

                })
    }

    /**
     * 获取首页之前的数据
     */
    fun getBeforNewsData(date: Long?) {
        homeModel.getBeforeNewsData(date)
                .subscribeOn(Schedulers.io())
                .doOnNext {
                    val helper = AppDataBaseHelper.newInstance(MyApplication.application!!)
                    val berforeStory = it.stories
                    if (!berforeStory.isEmpty()) {
                        val date = it.date
                        helper.insertOrUpDateStory(date, berforeStory as ArrayList<Story>)
                    }
                    val stories = helper.getStoryByDate(it.date)
                    if (!stories.isEmpty()) {
                        it.stories = stories
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .bindUntilEvent(owner!!, Lifecycle.Event.ON_DESTROY)
                .subscribe(object : BaseObserver<BeforeNewsData, HomeView>(homeView) {
                    override fun onNext(t: BeforeNewsData) {
                        homeView?.onBeforNewsData(t)
                    }

                })

    }

    /**
     * 无网络环境下获取更多story
     */
    fun getBeforeNewsDataOnNoNet(date: Long?) {
        Observable.create(ObservableOnSubscribe<BeforeNewsData> {
            val helper = AppDataBaseHelper.newInstance(MyApplication.application!!)
            val lastDate = getLastDay(date,1)
            LogUtils.d(msg="last day is: $lastDate")
            var stories: List<Story>? = null
            if (lastDate != null) {
                stories = helper.getStoryByDate(lastDate)
            }
            if (stories != null && !stories.isEmpty()) {
                val beforeNewsData = BeforeNewsData()
                beforeNewsData.date = lastDate
                beforeNewsData.stories = stories
                it.onNext(beforeNewsData)
                it.onComplete()
            } else {
                it.onError(Throwable("No Data in SQL"))
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindUntilEvent(owner!!, Lifecycle.Event.ON_DESTROY)
                .subscribe(object : BaseObserver<BeforeNewsData, HomeView>(homeView) {
                    override fun onNext(t: BeforeNewsData) {
                        homeView?.onBeforNewsData(t)
                    }

                })
    }
}