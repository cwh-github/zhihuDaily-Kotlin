package com.example.cwh_pc.dailynewsstudy.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.cwh_pc.dailynewsstudy.db.converter.dao.StoryDao
import com.example.cwh_pc.dailynewsstudy.db.converter.dao.StoryDetailsDao
import com.example.cwh_pc.dailynewsstudy.db.converter.dao.TopStoryDao
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsDetails
import com.example.cwh_pc.dailynewsstudy.model.entities.Story
import com.example.cwh_pc.dailynewsstudy.model.entities.TopStory


@Database(entities = [(Story::class), (TopStory::class), (NewsDetails::class)],version = 1,exportSchema = false)
abstract class AppDataBase:RoomDatabase(){
    abstract fun storyDao():StoryDao

    abstract fun storyDetailsDao():StoryDetailsDao

    abstract fun TopStoryDao():TopStoryDao

}