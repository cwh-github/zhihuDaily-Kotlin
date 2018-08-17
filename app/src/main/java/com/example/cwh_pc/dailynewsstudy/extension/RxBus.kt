package com.example.cwh_pc.dailynewsstudy.extension

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.reactivex.subscribers.SerializedSubscriber
import org.reactivestreams.Subscription

/**
 * 根据EventBus 通过RxJava2定义的RxBus来进行各个组件间数据的传递，解耦
 */

class RxBus{

    var _bus:Subject<Any>?=null

    var mDisposableMap:HashMap<String,CompositeDisposable>?=null

    init {
        _bus=PublishSubject.create<Any>().toSerialized()
    }

    companion object {
        @Volatile
       private var INSTANCE:RxBus?=null

        fun newInstance():RxBus{
            if(INSTANCE==null){
                synchronized(RxBus::class){
                    if(INSTANCE==null){
                        INSTANCE= RxBus()
                    }
                }
            }
            return INSTANCE!!
        }


    }

    /**
     * RxBus发送事件
     */
    fun post(o:Any){
        _bus?.onNext(o)
    }

    /**
     * 筛选符合要求的事件进行处理
     * 这里通过ofType的参数clazz来进行处理
     * 只有为clazz类型的才会发射数据出去
     *
     * 返回一个事件类型对应的Observable
     */
    fun <T> toObservable(clazz: Class<T>): Observable<T>{
        return _bus!!.ofType(clazz)
    }


    fun hasObserver():Boolean{
        return _bus!!.hasObservers()
    }

    /**
     * RxBus 接受消息进行处理
     * 一个默认的处理事件的方式
     */
    fun <T> doSubscribe(clazz:Class<T>,onNext:(T)->Unit,onError:(Throwable)->Unit,onComplete:()->Unit):Disposable{
        return toObservable(clazz)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onNext(it)
                },{
                    onError(it)
                },{
                    onComplete()
                })
    }


    /**
     * 添加Disposable,这样可以在相关生命周期的时候disposable
     */
    fun addDisposable(obj:Any,disposable: Disposable){
        if(mDisposableMap==null){
            mDisposableMap= HashMap()
        }

        if(mDisposableMap!![obj.javaClass.name] ==null){
            val compositeDisposable=CompositeDisposable()
            compositeDisposable.add(disposable)
            mDisposableMap!![obj.javaClass.name]=compositeDisposable
        }else{
            mDisposableMap!![obj.javaClass.name]!!.add(disposable)
        }

    }


    /**
     * 在相关生命周期结束时，取消相关的disposable
     */
    fun  removeDisposable(obj: Any){
        if(mDisposableMap==null){
            return
        }

        if(!mDisposableMap!!.contains(obj.javaClass.name)){
            return
        }

        val compositeDisposable=mDisposableMap!![obj.javaClass.name]
        compositeDisposable?.clear()
        mDisposableMap!!.remove(obj.javaClass.name)
    }



}