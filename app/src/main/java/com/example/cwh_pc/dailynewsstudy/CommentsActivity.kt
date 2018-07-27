package com.example.cwh_pc.dailynewsstudy

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.cwh_pc.dailynewsstudy.adapter.CommentsAdapter
import com.example.cwh_pc.dailynewsstudy.adapter.OnShortHeadClickListener
import com.example.cwh_pc.dailynewsstudy.extension.click
import com.example.cwh_pc.dailynewsstudy.extension.toast
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsCommentsData
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsLongComments
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsShortComments
import com.example.cwh_pc.dailynewsstudy.presenter.CommentsPresenter
import com.example.cwh_pc.dailynewsstudy.view.CommentsView
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : BaseActivity(), CommentsView {
    override fun onGetComments(newsCommentsData: NewsCommentsData) {
    }

    override fun onGetLongComments(newsLongComments: NewsLongComments) {
        if (adapter == null) {
            if (newsLongComments.comments.isEmpty()) {
                adapter = CommentsAdapter(this, null, null, longCount, shorCount)
            } else {
                adapter = CommentsAdapter(this, newsLongComments.comments, null, longCount, shorCount)
            }
            adapter!!.onShortHeadClickListener = shortClick
            comentRecylerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            comentRecylerView.adapter = adapter
        } else {
            adapter!!.longComments = newsLongComments.comments
            adapter!!.notifyDataSetChanged()
        }
    }

    val shortClick = object : OnShortHeadClickListener {
        override fun onShortClick(position: Int) {
            if(!hasLoadShortComment){
                shortHeadPosition=position
                presenter.getShortCommentsData(storyId)
            }
        }
    }

    override fun OnGetShortComments(newsShortComments: NewsShortComments) {
        if (adapter == null) {
            if (newsShortComments.comments.isEmpty()) {
                adapter = CommentsAdapter(this, null, null, longCount, shorCount)
            } else {
                adapter = CommentsAdapter(this, null, newsShortComments.comments, longCount, shorCount)
            }
            adapter!!.onShortHeadClickListener = shortClick
            comentRecylerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            comentRecylerView.adapter = adapter
        } else {
            adapter!!.shorComments = newsShortComments.comments
            adapter!!.notifyDataSetChanged()
        }
        comentRecylerView.scrollToPosition(shortHeadPosition)
        (comentRecylerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(shortHeadPosition,0)
        hasLoadShortComment=true
    }

    override fun onStartLoad() {
    }

    override fun onFailer(e: Throwable) {
        commentsRefresh.isRefreshing = false
        toast("加载评论出现错误，${e.message}")
    }

    override fun onComplete() {
        commentsRefresh.isRefreshing = false
        commentsRefresh.isEnabled = false
    }

    override fun onDestory() {
        presenter?.ondestory()

    }

    lateinit var presenter: CommentsPresenter
    var adapter: CommentsAdapter? = null
    var longCount = 0
    var shorCount = 0
    var storyId = 0L
    var shortHeadPosition=0
    var hasLoadShortComment=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        presenter = CommentsPresenter(this, this)
        imageCommentsBack.click { finish() }
        imageCommentWrite.click {
            toast("虽然可以点击，但是并不可以写评论ㄟ( ▔, ▔ )ㄏ")
        }
        storyId = intent.getLongExtra("story_id", 0L)
        val count = intent.getIntExtra("count", 0)
        longCount = intent.getIntExtra("long_count", 0)
        shorCount = intent.getIntExtra("short_count", 0)
        textCommentCount.text = "${count}条点评"
        if (storyId != 0L) {
            presenter.getLongCommentsData(storyId)
            commentsRefresh.setColorSchemeResources(R.color.colorPrimary)
            commentsRefresh.isRefreshing = true
            commentsRefresh.setOnRefreshListener {
                presenter.getLongCommentsData(storyId)
            }
        } else {
            toast("获取评论信息失败")
            commentsRefresh.isEnabled = false
        }
    }
}
