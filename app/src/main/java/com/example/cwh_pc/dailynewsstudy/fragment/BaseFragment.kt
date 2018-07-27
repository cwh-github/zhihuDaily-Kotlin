package com.example.cwh_pc.dailynewsstudy.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cwh_pc.dailynewsstudy.MainActivity
import com.example.cwh_pc.dailynewsstudy.extension.LogUtils

abstract class BaseFragment:Fragment() {


    var mActivity:AppCompatActivity?=null

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        mActivity=activity as MainActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.d("Fragment","onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        LogUtils.d("Fragment","Hidden Change $hidden")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    abstract fun onDestory()

    override fun onDestroy() {
        super.onDestroy()
        onDestory()
    }

}