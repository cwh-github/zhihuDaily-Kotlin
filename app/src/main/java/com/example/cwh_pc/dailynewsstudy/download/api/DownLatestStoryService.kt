package com.example.cwh_pc.dailynewsstudy.download.api

import com.example.cwh_pc.dailynewsstudy.model.entities.LatestNewsData
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

/**
 * 下载最新Story
 */
interface DownLatestStoryService {
    /**
     * 获取最新新闻(在首页显示)
     */
    @GET("news/latest")
    fun getLatestNews(): Call<LatestNewsData>
}