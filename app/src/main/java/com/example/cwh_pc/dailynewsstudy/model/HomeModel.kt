package com.example.cwh_pc.dailynewsstudy.model

import com.example.cwh_pc.dailynewsstudy.model.entities.BeforeNewsData
import com.example.cwh_pc.dailynewsstudy.model.entities.LatestNewsData
import com.example.cwh_pc.dailynewsstudy.model.entities.MenuData
import com.example.cwh_pc.dailynewsstudy.network.RetrofitNetWork
import com.example.cwh_pc.dailynewsstudy.network.netapi.BeforeNewsService
import com.example.cwh_pc.dailynewsstudy.network.netapi.LatestNewsService
import com.example.cwh_pc.dailynewsstudy.network.netapi.MenuService
import io.reactivex.Observable

class HomeModel {
    /**
     * 获取侧滑菜单栏数据
     */
    fun getMenuData(): Observable<MenuData> {
        val retrofit = RetrofitNetWork.newRetrofitInstance()
        val menuService = retrofit.create(MenuService::class.java)
        return menuService.getMenuItem()
    }

    /**
     * 获取最新的日报数据（在首页显示）
     */
    fun getLatestNewsData():Observable<LatestNewsData>{
        val retrofit = RetrofitNetWork.newRetrofitInstance()
        val latestNewsService=retrofit.create(LatestNewsService::class.java)
        return latestNewsService.getLatestNews()
    }

    /**
     * 获取之前的首页数据
     */
    fun getBeforeNewsData(date:Long?):Observable<BeforeNewsData>{
        val retrofit = RetrofitNetWork.newRetrofitInstance()
        val beforeNewsService=retrofit.create(BeforeNewsService::class.java)
        return beforeNewsService.getBeforeNewsData(date)
    }
}