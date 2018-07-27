package com.example.cwh_pc.dailynewsstudy.view

import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeData
import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeMoreData

interface OtherThemeView:IBaseView {
    fun onGetOtherThemeData(otherThemeData: OtherThemeData)

    fun onGetOtherThemeMoreData(otherThemeMoreData: OtherThemeMoreData)
}