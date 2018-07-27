package com.example.cwh_pc.dailynewsstudy.download.api

import com.example.cwh_pc.dailynewsstudy.model.entities.BeforeNewsData
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DownBeforeStoryService {
    //根据时间获取新闻，如获取2013 11 18号的新闻，如下地址
    //https://news-at.zhihu.com/api/4/news/before/20131119
    @GET("news/before/{date}")
    fun getBeforeNewsData(@Path("date")date:Long?): Call<BeforeNewsData>
}