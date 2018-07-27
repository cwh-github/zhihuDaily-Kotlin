package com.example.cwh_pc.dailynewsstudy.view

import com.example.cwh_pc.dailynewsstudy.model.entities.NewsCommentsData
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsLongComments
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsShortComments

interface CommentsView:IBaseView{

    fun onGetComments(newsCommentsData:NewsCommentsData)

    fun onGetLongComments(newsLongComments: NewsLongComments)

    fun OnGetShortComments(newsShortComments: NewsShortComments)
}