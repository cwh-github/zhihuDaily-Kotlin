package com.example.cwh_pc.dailynewsstudy.network.netapi


import com.example.cwh_pc.dailynewsstudy.model.entities.MenuData
import io.reactivex.Observable
import retrofit2.http.GET

interface MenuService {

    /**
     * 获取菜单栏条目和离线下载内容
     */
    @GET("themes")
    fun getMenuItem(): Observable<MenuData>

}