package com.example.cwh_pc.dailynewsstudy.adapter

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.cwh_pc.dailynewsstudy.R
import com.example.cwh_pc.dailynewsstudy.extension.click
import com.example.cwh_pc.dailynewsstudy.extension.loadCircleImage
import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeData
import kotlinx.android.synthetic.main.list_aditor_item.view.*


class ListEditorAdapter(var context:Context,var editors:ArrayList<OtherThemeData.EditorsBean>):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var itemClick:OnItemClick?=null
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.list_aditor_item,parent,false)
        return ListEditorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return editors.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val viewHolder=holder as ListEditorViewHolder
        val editor=editors[position]
        loadCircleImage(context,viewHolder.mEditorImage,editor.avatar,R.mipmap.comment_avatar,R.mipmap.comment_avatar)
        viewHolder.mEditorName.text= editor.name ?: ""
        viewHolder.mEditorBio.text=editor.bio ?: ""
        viewHolder.itemView.click {
            itemClick?.onItemClick(editor.id )
        }
    }

}

class ListEditorViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    var mEditorImage=itemView.find<ImageView>(R.id.mImageEditor)
    var mEditorName=itemView.find<TextView>(R.id.mNameText)
    var mEditorBio=itemView.find<TextView>(R.id.mBioText)
}

interface OnItemClick{
    fun onItemClick(id:Int)
}