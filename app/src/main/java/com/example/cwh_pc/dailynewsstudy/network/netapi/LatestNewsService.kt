package com.example.cwh_pc.dailynewsstudy.network.netapi


import com.example.cwh_pc.dailynewsstudy.model.entities.LatestNewsData
import io.reactivex.Observable
import retrofit2.http.GET

interface LatestNewsService {

    /**
     * 获取最新新闻(在首页显示)
     */
    @GET("news/latest")
    fun getLatestNews(): Observable<LatestNewsData>
}