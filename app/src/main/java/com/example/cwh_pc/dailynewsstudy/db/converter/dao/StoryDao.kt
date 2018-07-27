package com.example.cwh_pc.dailynewsstudy.db.converter.dao

import android.arch.persistence.room.*
import com.example.cwh_pc.dailynewsstudy.model.entities.Story

@Dao
interface StoryDao{

    /**
     * 根据date获取story
     */
    @Query("SELECT * FROM story WHERE date=:date order by order_num desc")
    fun getStoryByDate(date:Long):List<Story>

    @Query("SELECT * FROM story WHERE date=:date")
    fun getBeforeStoryByDate(date:Long):List<Story>

    @Query("SELECT * FROM story WHERE id=:id")
    fun getStoryById(id:Long):Story?

    /**
     * 获取指定日期之前的story
     */
    @Query("SELECT * FROM story WHERE date <:date")
    fun getStoryByDateLess(date:Long):List<Story>

    /**
     * 获取最近的story
     */
    @Query("SELECT * FROM story WHERE date = (SELECT MAX(date) from story) order by order_num desc")
    fun getLatsetStory():List<Story>

    /**
     * 插入条story
     */
    @Insert
    fun insert(story:Story)


    /**
     * 插入多条story
     */
    @Insert
    fun insert(strories:List<Story>)

    /**
     * 更新story
     */
    @Update
    fun update(story: Story)

    /**
     * 获取所有的收藏story
     */
    @Query("SELECT * FROM story WHERE isCollect=1 order by date desc ")
    fun getAllCollectStory():List<Story>


    /**
     * 删除stories
     */
    @Delete
    fun deleteStory(stories:List<Story>)

}