package com.example.cwh_pc.dailynewsstudy.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import com.example.cwh_pc.dailynewsstudy.db.AppDataBaseHelper
import com.example.cwh_pc.dailynewsstudy.extension.LogUtils
import com.example.cwh_pc.dailynewsstudy.extension.SharePreferencesUtils
import com.example.cwh_pc.dailynewsstudy.extension.getLessNDayDate
import com.example.cwh_pc.dailynewsstudy.extension.toast
import com.example.cwh_pc.dailynewsstudy.model.entities.Story
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class StoryDBPresenter(var context: Context, var owner: LifecycleOwner) {

    val compositeDisposable = CompositeDisposable()
    var listener: OnQueryListener? = null
    var collectListener: OnQueryCollectListener? = null

    /**
     * 更新最新story数据
     */
    fun insertOrUpDateLatestStory(date: Long, stories: ArrayList<Story>) {

        val instance = AppDataBaseHelper.newInstance(context)
        compositeDisposable.add(Observable.create(ObservableOnSubscribe<Story> {
            instance.insertOrUpDateStory(date, stories)
        })
                .bindUntilEvent(owner, Lifecycle.Event.ON_DESTROY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    context.toast("插入完成")
                })
        )
    }

    fun updateStory(story: Story) {
        val instance = AppDataBaseHelper.newInstance(context)
        compositeDisposable.add(Observable.create(ObservableOnSubscribe<Story> {
            instance.updateStory(story)
        })
                .bindUntilEvent(owner, Lifecycle.Event.ON_DESTROY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }

    fun getStoryById(id: Long) {
        val instance = AppDataBaseHelper.newInstance(context)
        compositeDisposable.add(Observable.create(ObservableOnSubscribe<Story?> {
            val story = instance.getStoryById(id)
            if (story == null) {
                it.onComplete()
            } else {
                it.onNext(story!!)
            }
        }).bindUntilEvent(owner, Lifecycle.Event.ON_DESTROY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listener?.onQuerySueecss(it!!)
                }, {

                }, {

                }))
    }


    fun getAllCollectStory() {
        val instance = AppDataBaseHelper.newInstance(context)
        compositeDisposable.add(Observable.create(ObservableOnSubscribe<List<Story>> {
            val stories = instance.getAllCollectStory()
            it.onNext(stories)
            it.onComplete()
        }).bindUntilEvent(owner, Lifecycle.Event.ON_DESTROY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    collectListener?.onQuerySuccess(it)
                }

        )
    }

    /**
     * 删除指定日期之前的Story
     */
    private fun deleteStoryByLessDate(date: Long) {
        val instance = AppDataBaseHelper.newInstance(context)
        compositeDisposable.add(Observable.create(ObservableOnSubscribe<Story> {
            instance.deleteStoryByDate(date)
            it.onComplete()
        })
                .bindUntilEvent(owner, Lifecycle.Event.ON_DESTROY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    context.toast("Delete OnNext")
                }, {
                    context.toast("Delete OnError")
                }, {
                    context.toast("Delete OnComplete")
                    SharePreferencesUtils.saveDeleteDate(context)
                }))

    }

    /**
     * 删除距离现在时间为N天以上的数据
     */
    fun deleteStoryByLessNDay(n: Int) {
        val date = getLessNDayDate(n)
        LogUtils.d(msg = "date is:$date")
        deleteStoryByLessDate(date)
    }

    interface OnQueryListener {
        fun onQuerySueecss(story: Story)
    }

    interface OnQueryCollectListener {
        fun onQuerySuccess(stories: List<Story>)
    }

    fun ondestory() {
        compositeDisposable.clear()
    }
}