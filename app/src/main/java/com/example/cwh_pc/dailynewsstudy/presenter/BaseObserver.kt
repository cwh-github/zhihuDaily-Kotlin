package com.example.cwh_pc.dailynewsstudy.presenter

import com.example.cwh_pc.dailynewsstudy.extension.LogUtils
import com.example.cwh_pc.dailynewsstudy.view.IBaseView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.observers.ResourceObserver

abstract class BaseObserver<T,V:IBaseView>(var view:V?): ResourceObserver<T>() {

    override fun onComplete() {
        view?.onComplete()
    }

    override fun onStart() {
        super.onStart()
        view?.onStartLoad()
    }

    override fun onError(e: Throwable) {
        LogUtils.d(msg="${e.message}")
        view?.onFailer(e)
    }
}