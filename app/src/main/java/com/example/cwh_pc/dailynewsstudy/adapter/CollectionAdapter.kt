package com.example.cwh_pc.dailynewsstudy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.cwh_pc.dailynewsstudy.R
import com.example.cwh_pc.dailynewsstudy.StoryDetailsActivity
import com.example.cwh_pc.dailynewsstudy.extension.click
import com.example.cwh_pc.dailynewsstudy.extension.loadImage
import com.example.cwh_pc.dailynewsstudy.model.entities.Story
import org.jetbrains.anko.startActivity

class CollectionAdapter(var stories:List<Story>,var context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_news,parent,false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val story=stories[position]
        val viewHolder=holder as NewsViewHolder
        viewHolder.textTitle.text=story.title
        loadImage(context,viewHolder.imageNews,story.images?.get(0))
        viewHolder.cardView.click {
            //todo enter other View
            context.startActivity<StoryDetailsActivity>("Story_Id" to story.id)
        }
    }

}
