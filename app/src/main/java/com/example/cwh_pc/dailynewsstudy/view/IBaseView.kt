package com.example.cwh_pc.dailynewsstudy.view

interface IBaseView {

    /**
     * 开始加载
     */
    fun onStartLoad()

    /**
     * 加载失败
     */
    fun onFailer(e: Throwable)


    /**
     * 加载完成
     */
    fun onComplete()
}