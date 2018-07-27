package com.example.cwh_pc.dailynewsstudy.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.example.cwh_pc.dailynewsstudy.model.OtherStoryModel
import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeData
import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeMoreData
import com.example.cwh_pc.dailynewsstudy.view.OtherThemeView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OtherThemePresenter(var otherThemeView: OtherThemeView, owner: LifecycleOwner) :
        IBasePresenter<OtherThemeView, LifecycleOwner>(otherThemeView, owner) {

    val model = OtherStoryModel()

    /**
     * 根据Theme id 获取story
     */
    fun getOtherStoryData(id:Int){
        model.getOtherThemeStory(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindUntilEvent(owner!!,Lifecycle.Event.ON_DESTROY)
                .subscribe(object :BaseObserver<OtherThemeData,OtherThemeView>(otherThemeView){
                    override fun onNext(t: OtherThemeData) {
                        otherThemeView?.onGetOtherThemeData(t)
                    }

                })
    }

    /**
     * 获取某个主题下的更多的文章，story_id为已显示的文章的最后一个的id
     */
    fun getOtherThemeMoreStory(id:Int,story_id:Long){
        model.getMoreOtherThemeStory(id,story_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindUntilEvent(owner!!,Lifecycle.Event.ON_DESTROY)
                .subscribe(object :BaseObserver<OtherThemeMoreData,OtherThemeView>(otherThemeView){
                    override fun onNext(t: OtherThemeMoreData) {
                        otherThemeView?.onGetOtherThemeMoreData(t)
                    }

                })
    }

}