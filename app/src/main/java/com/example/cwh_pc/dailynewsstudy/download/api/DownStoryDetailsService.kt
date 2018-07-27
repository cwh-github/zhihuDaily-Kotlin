package com.example.cwh_pc.dailynewsstudy.download.api

import com.example.cwh_pc.dailynewsstudy.model.entities.NewsDetails
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DownStoryDetailsService {
    @GET("news/{id}")
    fun getDetailsNews(@Path("id") id:Long): Call<NewsDetails>
}