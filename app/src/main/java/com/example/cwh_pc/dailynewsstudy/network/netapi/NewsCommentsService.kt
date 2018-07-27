package com.example.cwh_pc.dailynewsstudy.network.netapi

import com.example.cwh_pc.dailynewsstudy.model.entities.NewsCommentsData
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsLongComments
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsShortComments
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsCommentsService {
    /**
     * 获取评论数和点赞数等数据
     */
    //https://news-at.zhihu.com/api/4/story-extra/1848590
    @GET("story-extra/{id}")
    fun getNewsComments(@Path("id")id:Long):Observable<NewsCommentsData>


    /**
     * 获取长评论
     */
    //https://news-at.zhihu.com/api/4/story/8997528/long-comments
    @GET("story/{id}/long-comments")
    fun getNewsLongComments(@Path("id") id :Long):Observable<NewsLongComments>

    /**
     * 获取短评论
     */
    //https://news-at.zhihu.com/api/4/story/4232852/short-comments
    @GET("story/{id}/short-comments")
    fun getNewsShortComments(@Path("id") id:Long):Observable<NewsShortComments>

}