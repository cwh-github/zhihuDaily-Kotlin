package com.example.cwh_pc.dailynewsstudy.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.example.cwh_pc.dailynewsstudy.model.CommentModel
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsCommentsData
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsLongComments
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsShortComments
import com.example.cwh_pc.dailynewsstudy.view.CommentsView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CommentsPresenter(var commentsView: CommentsView,owner: LifecycleOwner):
        IBasePresenter<CommentsView,LifecycleOwner>(commentsView,owner){
    val model=CommentModel()

    /**
     * 获取评论数据
     */
    fun getCommentsData(id:Long){
        model.getNewsComments(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindUntilEvent(owner!!,Lifecycle.Event.ON_DESTROY)
                .subscribe(object :BaseObserver<NewsCommentsData,CommentsView>(commentsView){
                    override fun onNext(t: NewsCommentsData) {
                        commentsView?.onGetComments(t)
                    }

                })
    }

    /**
     * 获取长评论
     */
    fun getLongCommentsData(id:Long){
        model.getNewsLongComments(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindUntilEvent(owner!!,Lifecycle.Event.ON_DESTROY)
                .subscribe(object :BaseObserver<NewsLongComments,CommentsView>(commentsView){
                    override fun onNext(t: NewsLongComments) {
                        commentsView?.onGetLongComments(t)
                    }

                })
    }

    fun getShortCommentsData(id:Long){
        model.getNewsShortComments(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindUntilEvent(owner!!,Lifecycle.Event.ON_DESTROY)
                .subscribe(object : BaseObserver<NewsShortComments,CommentsView>(commentsView) {
                    override fun onNext(t: NewsShortComments) {
                        commentsView?.OnGetShortComments(t)
                    }

                })
    }
}