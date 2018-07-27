package com.example.cwh_pc.dailynewsstudy.view

import com.example.cwh_pc.dailynewsstudy.model.entities.NewsDetails

interface DetailsView:IBaseView {
    fun onDetilsSuccess(newsDetails: NewsDetails)
}