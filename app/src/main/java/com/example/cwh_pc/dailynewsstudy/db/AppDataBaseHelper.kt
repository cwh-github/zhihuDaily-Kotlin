package com.example.cwh_pc.dailynewsstudy.db

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.content.Context
import com.example.cwh_pc.dailynewsstudy.extension.LogUtils
import com.example.cwh_pc.dailynewsstudy.extension.downloadImageAsFile
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsDetails
import com.example.cwh_pc.dailynewsstudy.model.entities.Story
import com.example.cwh_pc.dailynewsstudy.model.entities.TopStory
import org.jetbrains.anko.collections.forEachWithIndex
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

class AppDataBaseHelper private constructor(var context: Context) {

    val appDataBase = Room.databaseBuilder(context.applicationContext,
            AppDataBase::class.java, "story.db")
            .build()

    companion object {
        @Volatile
        private var INSTANCE: AppDataBaseHelper? = null

        fun newInstance(context: Context): AppDataBaseHelper = INSTANCE ?: synchronized(this) {
            INSTANCE = AppDataBaseHelper(context.applicationContext)
            INSTANCE!!
        }
    }

    /**
     * 获取最近的story
     */
    fun getLatestStory():List<Story>{
        return appDataBase.storyDao().getLatsetStory()
    }

    /**
     * 根据时间获取Story
     */
    fun getStoryByDate(date: Long): List<Story> {
        return appDataBase.storyDao().getStoryByDate(date)
    }


    fun updateStory(story: Story){
        try {
            appDataBase.storyDao().update(story)
        }catch (e:Exception){
            e.printStackTrace()
            LogUtils.d(msg = "DataBase update Story error is:${e.message}")
        }
    }

    /**
     * 插入指定日期的stories
     */
    fun insertOrUpDateStory(date: Long, stories: ArrayList<Story>) {
        val hasStory = getStoryByDate(date)
        val ids: ArrayList<Long> = ArrayList()
        var maxOrder = 0
        hasStory.forEach {
            ids.add(it.id)
            maxOrder = if (maxOrder >= it.olderNum) maxOrder else it.olderNum
        }
        //反转stories
        stories.reverse()
        stories.forEach {
            if (!ids.contains(it.id)) {
                maxOrder++
                it.date=date
                it.olderNum = maxOrder
                appDataBase.storyDao().insert(it)
            }
        }
    }

    fun insertOrUpdateStoryByOffLine(date: Long, stories: ArrayList<Story>){
        val hasStory = getStoryByDate(date)
        val ids: ArrayList<Long> = ArrayList()
        var maxOrder = 0
        hasStory.forEach {
            ids.add(it.id)
            maxOrder = if (maxOrder >= it.olderNum) maxOrder else it.olderNum
        }
        //反转stories
        stories.reverse()
        stories.forEachWithIndex{index,story->
            if (!ids.contains(story.id)) {
                maxOrder++
                story.date=date
                story.olderNum = maxOrder
                appDataBase.storyDao().insert(story)
            }else{
                val ind=ids.indexOf(story.id)
                hasStory[ind].images=stories[index].images
                appDataBase.storyDao().update(hasStory[ind])
            }
        }
    }


    fun getStoryById(id:Long):Story?{
        return appDataBase.storyDao().getStoryById(id)
    }

    /**
     * 获取所有收藏的story
     */
    fun getAllCollectStory(): List<Story> {
        return appDataBase.storyDao().getAllCollectStory()
    }

    /**
     * 删除指定日期之前的story 和NewDetails 和离线下载缓存的图片内容
     */
    fun deleteStoryByDate(date: Long) {
        val stories = appDataBase.storyDao().getStoryByDateLess(date)
        try {
            appDataBase.storyDao().deleteStory(stories)
            deleteStoryImage(stories)
            stories.forEach{
                val dao=appDataBase.storyDetailsDao()
                val details=dao.getStoryDetailsForId(it.id)
                if(details!=null){
                    dao.delete(details)
                    deleteDetailsImage(details)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.d(msg = "DataBase Delete Story error is:${e.message}")
        }
    }

    private fun deleteStoryImage(stories: List<Story>){
        stories.forEach {
            val images=it.images
            if(images!=null && !images.isEmpty()){
                images.forEach {
                    if(!it.startsWith("https")){
                        val file= File(it)
                        if(file.exists())
                            file.delete()
                    }
                }
            }
        }
    }

    private fun deleteDetailsImage(newsDetails: NewsDetails){
       val localImages=newsDetails.localImages
        if(localImages!=null && !localImages.isEmpty()){
            localImages.forEach {
                val file= File(it)
                if(file.exists())
                    file.delete()
            }
        }
    }


    /**
     * 获取所有的TopStory
     */
    fun getAllTopStory(): List<TopStory> = appDataBase.TopStoryDao().getAllTopStory()

    /**
     * 删除所有topstory
     */
    fun deleteAllTopStory() {
        try {
            val allStory = getAllTopStory()
            appDataBase.TopStoryDao().deleteAllStory(allStory)
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.d(msg = "DataBase Delete TopStory error is:${e.message}")
        }
    }

    /**
     * 插入新的topstory
     */
    fun insertTopStory(stories: ArrayList<TopStory>) {
        try {
            if (getAllTopStory() == null || getAllTopStory().isEmpty()) {
                appDataBase.TopStoryDao().insert(stories)
            } else {
                deleteAllTopStory()
                appDataBase.TopStoryDao().insert(stories)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.d(msg = "DataBase insert TopStory error is:${e.message}")
        }
    }

    /**
     * 插入详细story
     */
    fun insertNewsDetails(newsDetails: NewsDetails){
        try{
        appDataBase.storyDetailsDao().insert(newsDetails)
        }catch (e:Exception){
            e.printStackTrace()
            LogUtils.d(msg = "DataBase insert  NewsDetails error is:${e.message}")
        }
    }

    fun insertOrUpdateDetails(newsDetails: NewsDetails){
        val details=appDataBase.storyDetailsDao().getStoryDetailsForId(newsDetails.id.toLong())
        if(details==null){
            appDataBase.storyDetailsDao().insert(newsDetails)
        }else{
            appDataBase.storyDetailsDao().update(newsDetails)
        }
    }

    /**
     * 获取NewDetails by id
     */
    fun getNewsDetailsById(id:Long):NewsDetails?{
        return appDataBase.storyDetailsDao().getStoryDetailsForId(id)
    }

}