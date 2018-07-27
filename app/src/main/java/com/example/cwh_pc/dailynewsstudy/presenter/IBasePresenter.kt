package com.example.cwh_pc.dailynewsstudy.presenter

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.app.AppCompatActivity
import com.example.cwh_pc.dailynewsstudy.view.IBaseView

abstract class IBasePresenter<V:IBaseView,T: LifecycleOwner>(var view:V?,var owner:T?) {

  open  fun ondestory(){
       view=null
       owner=null
   }
}