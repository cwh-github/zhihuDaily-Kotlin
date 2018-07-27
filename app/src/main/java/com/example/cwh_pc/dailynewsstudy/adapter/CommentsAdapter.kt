package com.example.cwh_pc.dailynewsstudy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.cwh_pc.dailynewsstudy.R
import com.example.cwh_pc.dailynewsstudy.extension.click
import com.example.cwh_pc.dailynewsstudy.extension.formatDate
import com.example.cwh_pc.dailynewsstudy.extension.loadCircleImage
import com.example.cwh_pc.dailynewsstudy.extension.toast

import com.example.cwh_pc.dailynewsstudy.model.entities.NewsLongComments
import com.example.cwh_pc.dailynewsstudy.model.entities.NewsShortComments

const val LONG_HEAD_TYPE = 0
const val EMPTY_TYPE = 1
const val SHORT_HEAD_TYPE = 2
const val NORMAL_COMMENTS_TYPE = 3

class CommentsAdapter(var context: Context, var longComments: List<NewsLongComments.CommentsBean>?,
                      var shorComments: List<NewsShortComments.CommentsBean>?, var longCount: Int, var shorCount: Int) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onShortHeadClickListener:OnShortHeadClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LONG_HEAD_TYPE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_long_comments, parent, false)
                LongCommentsHeadViewHolder(view)
            }

            EMPTY_TYPE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_empty_comment, parent, false)
                EmptyLongCommentsViewHolder(view)
            }

            SHORT_HEAD_TYPE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_long_comments, parent, false)

                ShortCommentsHeadViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_normal_comment, parent, false)
                CommentsViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return 2 + (longComments?.size ?: 1) + (shorComments?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            LONG_HEAD_TYPE -> {
                val viewHolder = holder as LongCommentsHeadViewHolder
                viewHolder.longText.text = "${longCount}条长评论"
            }

            SHORT_HEAD_TYPE -> {
                val viewHolder = holder as ShortCommentsHeadViewHolder
                viewHolder.itemView.click {
                    onShortHeadClickListener?.onShortClick(position)
                }
                viewHolder.longText.text = "${shorCount}条短评论"
            }

            NORMAL_COMMENTS_TYPE -> {
                val viewHolder = holder as CommentsViewHolder
                if (position <= (longComments?.size ?: 0)) {
                    val longComment = longComments!![position - 1]
                    loadCircleImage(context, viewHolder.mUserImage, longComment.avatar, R.mipmap.comment_avatar,
                            R.mipmap.comment_avatar)
                    viewHolder.userName.text = longComment.author
                    viewHolder.textVoteCount.text = longComment.likes.toString()
                    if (longComment.reply_to != null) {
                        val reply = longComment.reply_to
                        viewHolder.textComment.text = longComment.content + "\n\n${reply.author}: ${reply.content}"
                    } else {
                        viewHolder.textComment.text = longComment.content
                    }
                    viewHolder.commentTime.text= formatDate(longComment.time.toLong())
                }else{
                    val longCommentSize=longComments?.size ?:1
                    val shortComment=shorComments!![position-2-longCommentSize]
                    loadCircleImage(context, viewHolder.mUserImage, shortComment.avatar, R.mipmap.comment_avatar,
                            R.mipmap.comment_avatar)
                    viewHolder.userName.text = shortComment.author
                    viewHolder.textVoteCount.text = shortComment.likes.toString()
                    if (shortComment.reply_to != null) {
                        val reply = shortComment.reply_to
                        viewHolder.textComment.text = shortComment.content + "\n\n${reply.author}: ${reply.content}"
                    } else {
                        viewHolder.textComment.text = shortComment.content
                    }
                    viewHolder.commentTime.text= formatDate(shortComment.time.toLong())
                }

                viewHolder.textVoteCount.click {
                    context.toast("虽然可以点击，但是并没卵用ㄟ( ▔, ▔ )ㄏ")
                }
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> LONG_HEAD_TYPE
            (longComments == null || longComments!!.isEmpty()) && position == 1 -> EMPTY_TYPE
            position == (longComments?.size ?: 1) + 1 -> {
                SHORT_HEAD_TYPE
            }
            else -> {
                NORMAL_COMMENTS_TYPE
            }
        }
    }

}

class LongCommentsHeadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var longText = itemView.find<TextView>(R.id.textCount)

}

class ShortCommentsHeadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var longText = itemView.find<TextView>(R.id.textCount)
}

class EmptyLongCommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mUserImage = itemView.find<ImageView>(R.id.commentImage)
    var userName = itemView.find<TextView>(R.id.userName)
    var textVoteCount = itemView.find<TextView>(R.id.textVoteCount)
    var textComment = itemView.find<TextView>(R.id.textComment)
    var commentTime = itemView.find<TextView>(R.id.commentTime)
}

interface OnShortHeadClickListener{
    fun onShortClick(position: Int)
}