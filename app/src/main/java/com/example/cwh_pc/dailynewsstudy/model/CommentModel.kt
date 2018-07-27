package com.example.cwh_pc.dailynewsstudy.model

import com.example.cwh_pc.dailynewsstudy.model.entities.NewsCommentsData
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsLongComments
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsShortComments
import com.example.cwh_pc.dailynewsstudy.network.RetrofitNetWork
import com.example.cwh_pc.dailynewsstudy.network.netapi.NewsCommentsService
import io.reactivex.Observable

class CommentModel{
    /**
     * 获取评论数和点赞数等数据
     */
    //https://news-at.zhihu.com/api/4/story-extra/1848590
    fun getNewsComments(id:Long): Observable<NewsCommentsData>{
        val retrofit= RetrofitNetWork.newRetrofitInstance()
        val newsCommentsService=retrofit.create(NewsCommentsService::class.java)
        return newsCommentsService.getNewsComments(id)
    }

    /**
     * 获取长评论
     */
    fun getNewsLongComments(id :Long):Observable<NewsLongComments>{
        val retrofit= RetrofitNetWork.newRetrofitInstance()
        val newsCommentsService=retrofit.create(NewsCommentsService::class.java)
        return newsCommentsService.getNewsLongComments(id)
    }

    /**
     * 获取短评论
     */
    fun getNewsShortComments(id:Long):Observable<NewsShortComments>{
        val retrofit= RetrofitNetWork.newRetrofitInstance()
        val newsCommentsService=retrofit.create(NewsCommentsService::class.java)
        return newsCommentsService.getNewsShortComments(id)
    }
}