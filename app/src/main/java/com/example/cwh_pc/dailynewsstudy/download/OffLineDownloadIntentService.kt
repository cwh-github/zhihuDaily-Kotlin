package com.example.cwh_pc.dailynewsstudy.download

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.support.v4.content.LocalBroadcastManager
import com.example.cwh_pc.dailynewsstudy.db.AppDataBaseHelper
import com.example.cwh_pc.dailynewsstudy.download.api.DownBeforeStoryService
import com.example.cwh_pc.dailynewsstudy.download.api.DownLatestStoryService
import com.example.cwh_pc.dailynewsstudy.download.api.DownStoryDetailsService
import com.example.cwh_pc.dailynewsstudy.extension.LogUtils
import com.example.cwh_pc.dailynewsstudy.extension.downloadImageAsFile
import com.example.cwh_pc.dailynewsstudy.extension.getLastDay
import com.example.cwh_pc.dailynewsstudy.extension.toast
import com.example.cwh_pc.dailynewsstudy.model.entities.Story
import com.example.cwh_pc.dailynewsstudy.network.RetrofitNetWork
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStreamReader

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_DOWNLOAD = "com.example.cwh_pc.dailynewsstudy.download.action.download"

/**
 * 离线下载 10天的数据
 */
private const val DOWNLOAD_DAYS = 7
private const val MAX_TOTAL_COUNt=112

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class OffLineDownloadIntentService : IntentService("OffLineDownloadIntentService") {

    private val templateHtmlPath="template.html"
    var downloadCount=0
    var latestDate:Long=0
    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_DOWNLOAD -> {
                handleActionDownload()
            }
            else -> {
                LogUtils.d(msg = "Start Other Action")
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionDownload() {
        val intent=Intent()
        var currentDate: Long
        val helper = AppDataBaseHelper.newInstance(this.applicationContext)
        val retrofit = RetrofitNetWork.newRetrofitInstance()
        val downLatestStoryService = retrofit.create(DownLatestStoryService::class.java)
        val latestCall = downLatestStoryService.getLatestNews()
        try {
            val response = latestCall.execute()
            if (response.isSuccessful) {
                val latsetNewsData = response.body()
                if(latsetNewsData==null){
                    intent.action="com.download.error"
                    LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
                    return
                }
                val date = latsetNewsData.date
                if(date==null){
                    intent.action="com.download.error"
                    LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
                    return
                }
                latestDate=date
                currentDate = date
                if (latsetNewsData.stories != null) {
                    for (story in latsetNewsData.stories!!) {
                        if (story.images != null && !story.images!!.isEmpty()) {
                            val filePath = downloadImageAsFile(this.applicationContext, (story.images!![0]))
                            story.images!![0] = filePath
                        }
                    }
                    helper.insertOrUpdateStoryByOffLine(date, latsetNewsData.stories!!)
                }
                if (latsetNewsData.top_stories != null) {
                    for (topStory in latsetNewsData.top_stories!!) {
                        if (topStory.image != null) {
                            val filePath = downloadImageAsFile(this.applicationContext, topStory.image)
                            topStory.image = filePath
                        }
                    }
                    helper.insertTopStory(latsetNewsData.top_stories!!)
                }
                downloadStoryDetails(latsetNewsData.stories)
                downloadBeforeStory(currentDate)
            } else {
                toast("离线下载获取失败")
                intent.action="com.download.error"
                LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
                return
            }
        }catch (e:Exception){
            e.printStackTrace()
            toast("离线下载失败")
            LogUtils.d(msg="Down load error msg is:${e.message}")
            intent.action="com.download.error"
            LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
        }


    }

    /**
     * 下载新闻具体信息
     */
    private fun downloadStoryDetails(stories: ArrayList<Story>?) {
        if(stories==null || stories.isEmpty()){
            return
        }
        val intent=Intent()
        val html=getTemplateHtml()
        val helper = AppDataBaseHelper.newInstance(this.applicationContext)
        val retrofit = RetrofitNetWork.newRetrofitInstance()
        val downStoryDetailsService=retrofit.create(DownStoryDetailsService::class.java)
        for(story in stories){
            downloadCount++
            val callDetails=downStoryDetailsService.getDetailsNews(story.id)
            try {
                val responseDetails = callDetails.execute()
                if (responseDetails.isSuccessful) {
                    val newsDetails = responseDetails.body()
                    if (newsDetails != null && html != null) {
                        val content = newsDetails.body
                        val tempHtml = html.replace("{content}", content)
                        val doucument = Jsoup.parse(tempHtml)
                        val elements = doucument.getElementsByTag("img")
                        val localImages=ArrayList<String>()
                        for (element in elements) {
                            val imageUrl = element.attr("src")
                            val filePath = downloadImageAsFile(this.applicationContext, imageUrl)
                            element.attr("src", "file://$filePath")
                            localImages.add(filePath)
                        }
                        val newBody = doucument.body().toString()
                        newsDetails.body = newBody
                        newsDetails.localImages=localImages
                        val headImage=downloadImageAsFile(this.applicationContext,newsDetails.image)
                        newsDetails.image=headImage
                        helper.insertOrUpdateDetails(newsDetails)
                    }
                    intent.action="com.download.progress"
                    var process=(downloadCount * 100 / MAX_TOTAL_COUNt).toInt()
                    if(process>=100){
                        process=100
                    }
                    intent.putExtra("progress", process)
                    LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
                } else {
                    toast("离线下载获取具体新闻失败")
                    intent.action="com.download.progress"
                    var process=(downloadCount * 100 / MAX_TOTAL_COUNt).toInt()
                    if(process>=100){
                        process=100
                    }
                    intent.putExtra("progress", process)
                    LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
                    continue
                }
            }catch (e:Exception){
                e.printStackTrace()
                toast("离线下载具体新闻失败")
                LogUtils.d(msg="Down load error msg is:${e.message}")
                intent.action="com.download.error"
                LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
            }
        }

    }

    private fun downloadBeforeStory(currentDate: Long) {
        val intent=Intent()
        var date=currentDate
        val helper = AppDataBaseHelper.newInstance(this.applicationContext)
        val retrofit = RetrofitNetWork.newRetrofitInstance()
        val downBeforeStoryService=retrofit.create(DownBeforeStoryService::class.java)
        val tenDay=getLastDay(latestDate,DOWNLOAD_DAYS) ?: currentDate
        try {
            while (date>tenDay){
                val callBefore=downBeforeStoryService.getBeforeNewsData(date)
                val reponseBefor = callBefore.execute()
                if(reponseBefor.isSuccessful){
                    val beforeNewsData=reponseBefor.body()
                    if(beforeNewsData!=null && beforeNewsData.stories!=null &&
                            !beforeNewsData.stories.isEmpty()){
                        for(story in beforeNewsData.stories){
                            if (story.images != null && !story.images!!.isEmpty()) {
                                val filePath = downloadImageAsFile(this.applicationContext, (story.images!![0]))
                                story.images!![0] = filePath
                            }
                        }
                        helper.insertOrUpdateStoryByOffLine(beforeNewsData.date,beforeNewsData!!.stories as ArrayList<Story>)
                        downloadStoryDetails(beforeNewsData!!.stories as ArrayList<Story>)
                    }
                }else{
                    toast("离线下载获取新闻失败")
                }
                date= getLastDay(date) ?:currentDate
            }
            intent.action="com.download.progress"
            intent.putExtra("progress", 100)
            LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
        }catch (e:Exception){
            e.printStackTrace()
            toast("离线下载失败")
            LogUtils.d(msg="Down load error msg is:${e.message}")
            intent.action="com.download.error"
            LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(intent)
        }

    }


    private fun getTemplateHtml():String?{
        var sb=StringBuffer()
        try {
            BufferedReader(InputStreamReader(assets.open(templateHtmlPath))).use {
                var lineText:String?
                while (true){
                    lineText=it.readLine()
                    if (lineText!=null)
                        sb.append(lineText)
                    else
                        break
                }
            }
            return sb.toString()
        }catch (e:Exception){
            LogUtils.d(msg="get Assets error:${e.message}")
            return null
        }
    }


    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionDownload(context: Context) {
            val intent = Intent(context, OffLineDownloadIntentService::class.java).apply {
                action = ACTION_DOWNLOAD
            }
            context.startService(intent)
        }

    }
}
