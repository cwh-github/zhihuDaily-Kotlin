package com.example.cwh_pc.dailynewsstudy.presenter

import com.example.cwh_pc.dailynewsstudy.view.IBaseView
import io.reactivex.disposables.CompositeDisposable


/**
 * 原来用来取消绑定的方式
 */
abstract class IBasePresenterLoal<V:IBaseView>(var view:V){

    var compositeDisposable:CompositeDisposable?=null

    fun onCreate(){
        if(compositeDisposable==null){
            compositeDisposable=CompositeDisposable()
        }
    }


    fun onDestory(){
        if(compositeDisposable!=null && !compositeDisposable!!.isDisposed){
            compositeDisposable!!.clear()
        }

    }





}