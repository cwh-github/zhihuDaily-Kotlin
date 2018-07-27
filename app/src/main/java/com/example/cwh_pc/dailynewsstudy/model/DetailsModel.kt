package com.example.cwh_pc.dailynewsstudy.model

import com.example.cwh_pc.dailynewsstudy.model.entities.NewsDetails
import com.example.cwh_pc.dailynewsstudy.network.RetrofitNetWork
import com.example.cwh_pc.dailynewsstudy.network.netapi.NewsDetalisService
import io.reactivex.Observable

class DetailsModel{

    /**
     * 获取News details
     */
    fun getDetails(id:Long):Observable<NewsDetails>{
        val retrofit=RetrofitNetWork.newRetrofitInstance()
        val newsDetalisService=retrofit.create(NewsDetalisService::class.java)
        return newsDetalisService.getDetailsNews(id)
    }
}