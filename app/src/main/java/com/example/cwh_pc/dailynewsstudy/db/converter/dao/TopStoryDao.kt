package com.example.cwh_pc.dailynewsstudy.db.converter.dao

import android.arch.persistence.room.*
import com.example.cwh_pc.dailynewsstudy.model.entities.Story
import com.example.cwh_pc.dailynewsstudy.model.entities.TopStory


@Dao
interface TopStoryDao{

    @Query("SELECT * FROM top_story")
    fun getAllTopStory():List<TopStory>

    @Delete
    fun deleteAllStory(stories:List<TopStory>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stories: ArrayList<TopStory>)

}