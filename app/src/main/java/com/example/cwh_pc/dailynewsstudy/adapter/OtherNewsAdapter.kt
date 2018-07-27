package com.example.cwh_pc.dailynewsstudy.adapter

import android.content.Context
import android.media.Image
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.cwh_pc.dailynewsstudy.EditorsActivity
import com.example.cwh_pc.dailynewsstudy.R
import com.example.cwh_pc.dailynewsstudy.StoryDetailsActivity
import com.example.cwh_pc.dailynewsstudy.extension.click
import com.example.cwh_pc.dailynewsstudy.extension.loadImage
import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeData
import kotlinx.android.synthetic.main.other_theme_top_head.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.w3c.dom.Text

const val HEAD_THEME_TYPE=0
const val NO_IMAGE_THEME=1
const val IMAGE_THEHE=2

class OtherNewsAdapter(var otherThemeData: OtherThemeData?,var stories:ArrayList<OtherThemeData.StoriesBean>?,
                       var editors:ArrayList<OtherThemeData.EditorsBean>?,var context:Context):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val Loading=100
    private val Loading_Complete=101
    private val Loading_Fail=102
    private var loadState=100
    private var isLoading=false

    var loadMoreListener:OtherThemeLoadMoreListener?=null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType){
            HEAD_THEME_TYPE->{
                val view= LayoutInflater.from(context).inflate(R.layout.other_theme_top_head,parent,false)
                OtherHeadViewHolder(view)
            }

            FOOT_TYPE->{
                val view=LayoutInflater.from(context).inflate(R.layout.loading_more_view,parent,false)
                LoadMoreViewHolder(view)
            }

            NO_IMAGE_THEME->{
                val view=LayoutInflater.from(context).inflate(R.layout.other_theme_no_image,parent,false)
                NoImageViewHolder(view)
            }

            else -> {
                val view= LayoutInflater.from(context).inflate(R.layout.item_news,parent,false)
                ImageViewHolder(view)
            }
        }

    }



    fun loadFail(){
        loadState=Loading_Fail
        isLoading=false
        notifyItemChanged(itemCount-1)
    }

    fun loadComplete(){
        isLoading=false
    }

    private fun loading(){
        loadState=Loading
        notifyItemChanged(itemCount-1)
    }

    override fun getItemCount(): Int {
        if(stories==null || stories!!.size==0){
            return 1
        }
        return stories!!.size+2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when(getItemViewType(position)){
            HEAD_THEME_TYPE->{
                val viewHolder= holder as OtherHeadViewHolder
                loadImage(context,viewHolder.otherImage,otherThemeData?.background,errorid = R.drawable.otehr_theme_load_image,
                        placeholderId = R.drawable.otehr_theme_load_image)
                viewHolder.textSOurce.text=otherThemeData?.image_source ?: ""
                viewHolder.textTitle.text=otherThemeData?.description ?: " "
                val recyclerView=viewHolder.authorRecyclerView
                recyclerView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                if(editors!=null){
                    val mAdapter=EditorsAdapter(editors!!,context)
                    mAdapter.onGoListener=object :OnGoListListener{
                        override fun onGoList() {
                            context.startActivity<EditorsActivity>("editors" to editors)
                        }

                    }
                    recyclerView.adapter=mAdapter
                }
            }

            FOOT_TYPE->{
                val viewHolder= holder as LoadMoreViewHolder
                if(loadState==Loading){
                    viewHolder.loadingView.visibility=View.VISIBLE
                    viewHolder.loadingFailView.visibility=View.GONE
                    if(!isLoading){
                        isLoading=true
                        loadMoreListener?.loadMore()
                    }
                }else{
                    viewHolder.loadingView.visibility=View.GONE
                    viewHolder.loadingFailView.visibility=View.VISIBLE
                    viewHolder.loadingFailView.click {
                        loading()
                    }

                }
            }


            NO_IMAGE_THEME->{
                val story=stories!![position-1]
                val viewHolder=holder as NoImageViewHolder
                viewHolder.textThemeNoImage.text=story.title
                viewHolder.relaCardView.click {
                    context.startActivity<StoryDetailsActivity>("Story_Id" to story.id.toLong(),
                            "tag" to "Theme")
                }
            }

            else->{
                val story=stories!![position-1]
                val viewHolder=holder as ImageViewHolder
                viewHolder.textTitle.text=story.title
                loadImage(context,viewHolder.imageNews,story.images?.get(0))
                viewHolder.cardView.click {
                    context.startActivity<StoryDetailsActivity>("Story_Id" to story.id.toLong(),
                            "image_url" to (story.images?.get(0) ?: ""),"tag" to "Theme")
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when{
            position==0-> HEAD_THEME_TYPE
            position==itemCount-1->FOOT_TYPE
            stories!![position-1].images==null-> NO_IMAGE_THEME
            else -> {
                IMAGE_THEHE
            }
        }
    }

}

class OtherHeadViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    var otherImage=itemView.find<ImageView>(R.id.otherImage)
    var textSOurce=itemView.find<TextView>(R.id.textSource)
    var textTitle=itemView.find<TextView>(R.id.textTitle)
    var authorRecyclerView=itemView.find<RecyclerView>(R.id.authorRecylerView)

}

class NoImageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var relaCardView=itemView.find<RelativeLayout>(R.id.noImageCardView)
    var textThemeNoImage=itemView.find<TextView>(R.id.textThemeNoImage)
}

class ImageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var cardView=itemView.find<RelativeLayout>(R.id.cardView)
    var textTitle=itemView.find<TextView>(R.id.textTitle)
    var imageNews=itemView.find<ImageView>(R.id.imageNews)
}

interface OtherThemeLoadMoreListener{
    fun loadMore()
}