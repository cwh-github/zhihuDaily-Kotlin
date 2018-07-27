package com.example.cwh_pc.dailynewsstudy.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.example.cwh_pc.dailynewsstudy.MyApplication
import com.example.cwh_pc.dailynewsstudy.db.AppDataBaseHelper
import com.example.cwh_pc.dailynewsstudy.model.DetailsModel
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsDetails
import com.example.cwh_pc.dailynewsstudy.view.DetailsView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsStoryDetailsPresenter(var detailsView: DetailsView?, owner:LifecycleOwner):
        IBasePresenter<DetailsView,LifecycleOwner>(detailsView,owner) {

    val detailsModel=DetailsModel()

    /**
     * 根据ID获取details
     */
    fun getDetails(id:Long){
        detailsModel.getDetails(id)
                .subscribeOn(Schedulers.io())
                .doOnNext {
                    val helper=AppDataBaseHelper.newInstance(MyApplication.application!!)
                    helper.insertNewsDetails(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .bindUntilEvent(owner!!,Lifecycle.Event.ON_DESTROY)
                .subscribe(object :BaseObserver<NewsDetails,DetailsView>(detailsView){
                    override fun onNext(t: NewsDetails) {
                        detailsView?.onDetilsSuccess(t)
                    }

                })
    }

    fun getDetailsOnNoNet(id:Long){
        Observable.create(ObservableOnSubscribe <NewsDetails>{
            val helper=AppDataBaseHelper.newInstance(MyApplication.application!!)
            val newsDetails=helper.getNewsDetailsById(id)
            if(newsDetails!=null){
                it.onNext(newsDetails)
                it.onComplete()
            }else{
                it.onError(Throwable("No Data in SQL"))
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindUntilEvent(owner!!,Lifecycle.Event.ON_DESTROY)
                .subscribe(object :BaseObserver<NewsDetails,DetailsView>(detailsView){
                    override fun onNext(t: NewsDetails) {
                        detailsView?.onDetilsSuccess(t)
                    }

                })
    }
}