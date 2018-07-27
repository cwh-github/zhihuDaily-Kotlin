package com.example.cwh_pc.dailynewsstudy.model

import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeData
import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeMoreData
import com.example.cwh_pc.dailynewsstudy.network.RetrofitNetWork
import com.example.cwh_pc.dailynewsstudy.network.netapi.OtherThemeService
import io.reactivex.Observable

class OtherStoryModel {
    /**
     * 获取某个主题下的文章
     */
    fun getOtherThemeStory(id:Int): Observable<OtherThemeData>{
        val retrofit=RetrofitNetWork.newRetrofitInstance()
        val otherThemeService=retrofit.create(OtherThemeService::class.java)
        return otherThemeService.getOtherThemeStory(id)
    }

    /**
     * 获取某个主题下的跟多文章，story_id为已显示的文章的最后一个的id
     */
    fun getMoreOtherThemeStory(id:Int,story_id:Long):Observable<OtherThemeMoreData>{
        val retrofit=RetrofitNetWork.newRetrofitInstance()
        val otherThemeService=retrofit.create(OtherThemeService::class.java)
        return otherThemeService.getMoreOtherThemeStory(id,story_id)
    }
}