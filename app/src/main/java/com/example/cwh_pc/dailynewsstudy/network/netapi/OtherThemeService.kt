package com.example.cwh_pc.dailynewsstudy.network.netapi

import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeData
import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeMoreData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface OtherThemeService {

    @GET("theme/{id}")
    fun getOtherThemeStory(@Path("id")id:Int):Observable<OtherThemeData>

    /**
     * 查看之前的消息
     * https://news-at.zhihu.com/api/4/theme/{theme_id}/before/{story_id}
     *  theme_id为主题id
     *  story_id为一获取的stories的最后一个的ID
    **/

    @GET("theme/{theme_id}/before/{story_id}")
    fun getMoreOtherThemeStory(@Path("theme_id") id:Int,@Path("story_id") story_id:Long):Observable<OtherThemeMoreData>
}