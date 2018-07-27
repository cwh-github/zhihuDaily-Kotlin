package com.example.cwh_pc.dailynewsstudy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.cwh_pc.dailynewsstudy.R
import com.example.cwh_pc.dailynewsstudy.extension.click
import com.example.cwh_pc.dailynewsstudy.extension.loadCircleImage
import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeData

class EditorsAdapter(var editors: List<OtherThemeData.EditorsBean>,var context:Context):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var onGoListener:OnGoListListener?=null
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_editors,parent,false)
        return EditorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return editors.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val viewHolder=holder as EditorViewHolder
        loadCircleImage(context,viewHolder.editorImage,editors[position].avatar)
        viewHolder.itemView.click {
            onGoListener?.onGoList()
        }
    }

}

class EditorViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    var editorImage=itemView.find<ImageView>(R.id.authorImage)
}

interface OnGoListListener{
    fun onGoList()
}