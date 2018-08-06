package com.example.cwh_pc.dailynewsstudy

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.example.cwh_pc.dailynewsstudy.extension.*
import com.example.cwh_pc.dailynewsstudy.model.entities.*
import com.example.cwh_pc.dailynewsstudy.presenter.CommentsPresenter
import com.example.cwh_pc.dailynewsstudy.presenter.NewsStoryDetailsPresenter
import com.example.cwh_pc.dailynewsstudy.presenter.StoryDBPresenter
import com.example.cwh_pc.dailynewsstudy.view.CommentsView
import com.example.cwh_pc.dailynewsstudy.view.DetailsView
import com.like.LikeButton
import com.like.OnLikeListener
import kotlinx.android.synthetic.main.activity_story_details.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.startActivity
import java.io.BufferedReader
import java.io.InputStreamReader


class StoryDetailsActivity : BaseActivity(), DetailsView, CommentsView ,StoryDBPresenter.OnQueryListener{


    override fun onQuerySueecss(s: Story) {
        //LogUtils.d(msg="story is $s")
        likeCollect.isEnabled=true
        likeCollect.isLiked = s.iscollect==1
        story=s
    }

    override fun onGetLongComments(newsLongComments: NewsLongComments) {
    }

    override fun OnGetShortComments(newsShortComments: NewsShortComments) {
    }

    override fun onGetComments(newsCommentsData: NewsCommentsData) {
        textlinke.text = newsCommentsData.popularity.toString()
        imageComment.text = newsCommentsData.comments.toString()

        imageComment.click {
            startActivity<CommentsActivity>("count" to newsCommentsData.comments,
                    "story_id" to storyId ,"long_count" to newsCommentsData.long_comments,
                    "short_count" to newsCommentsData.short_comments)

        }
    }

    override fun onDetilsSuccess(newsDetails: NewsDetails) {
        //load Story
        refreshLayout.isRefreshing = false
        refreshLayout.isEnabled = false
        loadStory(newsDetails)

    }

    private fun loadStory(newsDetails: NewsDetails) {
        if (!TextUtils.isEmpty(newsDetails.image)) {
            textSource.text = newsDetails.image_source
            textTitle.text = newsDetails.title
            loadImage(this, imageTop, newsDetails.image)
        } else {
            if (!TextUtils.isEmpty(imageUrl)) {
                loadImage(this, imageTop, imageUrl)
            } else {
                rela.visibility = View.GONE
                val laparams = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                        dip(56))
                appBar.layoutParams = laparams
            }

        }
        webView.isVerticalScrollBarEnabled = true
        webView.settings.javaScriptEnabled=true
        var html=getTemplateHtml()
        val body:String
        if (tag == "Theme") {
            body= HtmlUtil.createHtmlWithHead(newsDetails.css,
                    newsDetails.js, newsDetails.body)
        } else {
            body=HtmlUtil.createHtmlData(newsDetails.css,newsDetails.js,newsDetails.body)
        }

        //load body内容到自己定义的Html中
        if(html==null){
            webView.loadDataWithBaseURL("",body, "text/html", "utf-8", null)
        }else{
            html=html.replace("{content}",body)
            html=html.replace("{nightTheme}",if(SharePreferencesUtils.getIsNightTheme(this)) "true" else "false")
            //LogUtils.d(msg="Html: $html")
            webView.loadDataWithBaseURL("",html, "text/html", "utf-8", null)
        }
    }
    private val templateHtmlPath="template.html"
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

    override fun onStartLoad() {
    }

    override fun onFailer(e: Throwable) {
        refreshLayout.isRefreshing = false
        refreshLayout.isEnabled = true
        toast(msg = "获取内容出现错误。${e.message}")
    }

    override fun onComplete() {
        refreshLayout.isRefreshing = false
        refreshLayout.isEnabled = false
    }

    override fun onDestory() {
        presenter?.ondestory()
        commentPresenter?.ondestory()
        dbPresenter?.ondestory()
        if (webView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            val parent = webView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(webView)
            }
            webView.stopLoading()
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.settings.javaScriptEnabled = false
            webView.clearHistory()
            webView.clearView()
            webView.removeAllViews()

            try {
                webView.destroy()
            } catch (ex: Throwable) {

            }

        }
    }

    var storyId: Long = -1L
    var imageUrl: String? = null
    var tag: String? = null
    lateinit var presenter: NewsStoryDetailsPresenter
    lateinit var commentPresenter: CommentsPresenter
    lateinit var dbPresenter: StoryDBPresenter
    var story:Story?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_details)
        storyId = intent.getLongExtra("Story_Id", -1L)
        imageUrl = intent.getStringExtra("image_url")
        tag = intent.getStringExtra("tag")
        presenter = NewsStoryDetailsPresenter(this, this)
        commentPresenter = CommentsPresenter(this, this)
        dbPresenter= StoryDBPresenter(this,this)
        dbPresenter.listener=this
        if (storyId == -1L) {
            toast("获取文章内容失败")
            refreshLayout.isEnabled = false
        } else {
            if(NetWorkUtils.isConnectNet()){
                presenter.getDetails(storyId)
                commentPresenter.getCommentsData(storyId)
            }else{
                presenter.getDetailsOnNoNet(storyId)
            }
            refreshLayout.isRefreshing = true
            refreshLayout.setOnRefreshListener {
                if(!NetWorkUtils.isConnectNet()){
                    refreshLayout.isRefreshing=false
                    toast("暂无网络")
                    return@setOnRefreshListener
                }
                presenter.getDetails(storyId)
            }
            dbPresenter.getStoryById(storyId)
        }

        imageBack.click {
            finish()
        }
        if(tag=="Theme"){
            likeCollect.visibility=View.INVISIBLE
        }



        likeCollect.setOnLikeListener(object :OnLikeListener{
            override fun liked(p0: LikeButton?) {
                if(story!=null){
                    toast("like")
                    story!!.iscollect=1
                    dbPresenter.updateStory(story!!)
                }
            }

            override fun unLiked(p0: LikeButton?) {
                if(story!=null){
                    toast("unlike")
                    story!!.iscollect=0
                    dbPresenter.updateStory(story!!)
                }
            }

        })

        likeButton.setOnLikeListener(object :OnLikeListener{
            override fun liked(p0: LikeButton?) {
              this@StoryDetailsActivity.toast("虽然可以点，但并没卵用ㄟ( ▔, ▔ )ㄏ")
            }

            override fun unLiked(p0: LikeButton?) {
                this@StoryDetailsActivity.toast("虽然可以点，但并没卵用ㄟ( ▔, ▔ )ㄏ")
            }

        })
//
//        likeCollect.click {
//            toast("虽然可以点，但并没卵用ㄟ( ▔, ▔ )ㄏ")
//        }


    }
}
