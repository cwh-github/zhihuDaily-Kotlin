package com.example.cwh_pc.dailynewsstudy.network.netapi

import com.example.cwh_pc.dailynewsstudy.model.entities.NewsDetails
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsDetalisService {

    @GET("news/{id}")
    fun getDetailsNews(@Path("id") id:Long):Observable<NewsDetails>
}