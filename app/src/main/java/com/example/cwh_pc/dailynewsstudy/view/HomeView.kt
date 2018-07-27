package com.example.cwh_pc.dailynewsstudy.view

import com.example.cwh_pc.dailynewsstudy.model.entities.BeforeNewsData
import com.example.cwh_pc.dailynewsstudy.model.entities.LatestNewsData
import com.example.cwh_pc.dailynewsstudy.model.entities.MenuData

interface HomeView:IBaseView {
    fun onMenuData(menuData: MenuData)

    fun onLatestNewsData(latestNewsData: LatestNewsData)

    fun onBeforNewsData(beforeNewsData: BeforeNewsData)
}