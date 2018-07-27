package com.example.cwh_pc.dailynewsstudy.db.converter.dao

import android.arch.persistence.room.*
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsDetails

@Dao
interface StoryDetailsDao{

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    fun insert(storyDetails:ArrayList<NewsDetails>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(storyDetail: NewsDetails)

    @Update
    fun update(storyDetail: NewsDetails)

    @Query("SELECT * FROM news_details WHERE id=:id")
    fun getStoryDetailsForId(id:Long):NewsDetails

    @Delete
    fun delete(storyDetails: ArrayList<NewsDetails>)

    @Delete
    fun delete(storyDetail: NewsDetails)
}