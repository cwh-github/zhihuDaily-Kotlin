package com.example.cwh_pc.dailynewsstudy.adapter

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.os.Handler
import android.os.Message
import android.provider.ContactsContract
import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.cwh_pc.dailynewsstudy.R
import com.example.cwh_pc.dailynewsstudy.StoryDetailsActivity
import com.example.cwh_pc.dailynewsstudy.extension.*
import com.example.cwh_pc.dailynewsstudy.model.entities.Story
import com.example.cwh_pc.dailynewsstudy.model.entities.TopStory
import com.example.cwh_pc.dailynewsstudy.presenter.StoryDBPresenter
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit



const val HEAD_TYPE=0
const val TIME_TYPE=1
const val ITME_NEWS_TYPE=2
const val FOOT_TYPE=3
class HomeNewsAdapter(var newsData: ArrayList<Story>?,var topNews:ArrayList<TopStory>?,var context:Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var currentIndex=0
    var mDispose:Disposable?=null
    private val Loading=100
    private val Loading_Complete=101
    private val Loading_Fail=102
    var loadState=100
    var isLoading=false

    var loadMoreListener:LoadMoreListener?=null
    val dbPresenter=StoryDBPresenter(context,context as LifecycleOwner)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType){
            HEAD_TYPE->{
                val view=LayoutInflater.from(context).inflate(R.layout.home_top_story,parent,false)
                HeadViewViewHolder(view)
            }
           TIME_TYPE ->{
               val view=LayoutInflater.from(context).inflate(R.layout.item_date,parent,false)
               DateViewHolder(view)
           }

           FOOT_TYPE->{
                createLoadMoreView(parent)
               }
           else -> {
               val view=LayoutInflater.from(context).inflate(R.layout.item_news,parent,false)
               NewsViewHolder(view)
           }
       }
    }

    override fun getItemCount(): Int {
        //由于包含一个head和一个footer，所以加2
        if(newsData==null || newsData!!.size==0){
            return 1
        }
        return newsData!!.size+2
    }

    fun createLoadMoreView(parent: ViewGroup?):LoadMoreViewHolder{
        return when{
            loadState==Loading->{
                val view=LayoutInflater.from(context).inflate(R.layout.loading_more_view,parent,false)
                LoadMoreViewHolder(view)
            }

            loadState==Loading_Fail->{
                val view=LayoutInflater.from(context).inflate(R.layout.loading_more_view,parent,false)
                LoadMoreViewHolder(view)
            }

            else -> {
                val view=LayoutInflater.from(context).inflate(R.layout.loading_more_view,parent,false)
                LoadMoreViewHolder(view)
            }
        }
    }

    fun loadFail(){
        loadState=Loading_Fail
        isLoading=false
        //此方法只会重走onBindViewHolder方法
        notifyItemChanged(itemCount-1)
        LogUtils.d(msg="Load Fail call")
    }
    fun loadComplete(){
        isLoading=false
    }

    private fun loading(){
        loadState=Loading
        notifyItemChanged(itemCount-1)
    }





    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        super.onViewRecycled(holder)
        if(holder is HeadViewViewHolder){
            mDispose?.dispose()
            LogUtils.d(msg="Dispose Thread")
        }
    }

    fun getDateTitle(position: Int):String{
        return if(newsData!=null){
            val story=newsData!![position-1]
            if(getCurrentDate()==story.date.toString()){
                "今日热闻"
            }else{
                getMonthAndWeek(story.date)
            }
        }else{
            " "
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when(getItemViewType(position)){
            HEAD_TYPE->{
                val viewHolder=holder as HeadViewViewHolder
                createHeadView(viewHolder.viewPager,viewHolder.tapLinear)
            }

            TIME_TYPE->{
                val story=newsData!![position-1]
                val viewHolder=holder as DateViewHolder
                viewHolder.dateText.text=if(getCurrentDate()==story.date.toString()) {
                    "今日热闻"
                }
                else
                    getMonthAndWeek(story.date)

            }

            FOOT_TYPE->{
                LogUtils.d(msg="create Load More Fail View notify")
                val viewHolder= holder as LoadMoreViewHolder
                if(loadState==Loading){
                    viewHolder.loadingView.setVerticalGravity(View.VISIBLE)
                    viewHolder.loadingFailView.visibility=View.GONE
                    if(!isLoading){
                        loadMoreListener?.loadingMore()
                        isLoading=true
                    }
                } else{
                    viewHolder.loadingView.visibility=View.GONE
                    viewHolder.loadingFailView.visibility=View.VISIBLE
                    viewHolder.loadingFailView.click {
                        loading()
                    }
                }
            }

            ITME_NEWS_TYPE->{
                val story=newsData!![position-1]
                val viewHolder=holder as NewsViewHolder
                viewHolder.textTitle.text=story.title
                loadImage(context,viewHolder.imageNews, story.images?.get(0))
                viewHolder.cardView.click {
                   //todo enter other View
                    context.startActivity<StoryDetailsActivity>("Story_Id" to story.id)
                    if(story.isread==0){
                        story.isread=1
                        notifyItemChanged(position)
                        dbPresenter.updateStory(story)
                    }
                }
                if(story.isread==1){
                    viewHolder.textTitle.setTextColor(context.resources.getColor(R.color.read_color))
                    viewHolder.cardView.backgroundResource=R.drawable.item_read_click_ripple
                }else{
                    viewHolder.textTitle.textColor=context.resources.getColor(R.color.splash_bg_color)
                    viewHolder.cardView.backgroundResource=R.drawable.item_click_ripple
                }
            }
        }

    }

    private fun createHeadView(viewPager: ViewPager, tapLinear: LinearLayout) {
        val topAdapter=TopViewPagerAdapter(topNews,context)
        topAdapter.listener=object:ViewPagerClickListener{
            override fun onItemClick(id: Long?) {
                //context.toast(msg="id is $id")
                //todo enter other View
                context.startActivity<StoryDetailsActivity>("Story_Id" to id)
            }

        }
        viewPager.adapter=topAdapter
        if((topNews?.size ?: 0)>0){
            LogUtils.d(msg="create HeadView")
            viewPager.currentItem = currentIndex

            mDispose=Flowable.interval(3,TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer {
                        if(currentIndex<(topNews?.size ?:0)-1){
                            currentIndex += 1
                        }else{
                            currentIndex=0
                        }
                        viewPager?.currentItem=currentIndex
                    })
            tapLinear.removeAllViews()
            for(i in 0 until (topNews?.size ?: 0)){
                val imageView=ImageView(context)
                val laprams=LinearLayout.LayoutParams(context.dip(6),
                        context.dip(6))
                laprams.gravity=Gravity.CENTER
                laprams.rightMargin=context.dip(6)
                imageView.layoutParams=laprams
                imageView.setImageResource(R.drawable.indicator_normal)
                tapLinear.addView(imageView)
            }
            val view=tapLinear.getChildAt(currentIndex)
            (view as ImageView).setImageResource(R.drawable.indicator_check)

        }
        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentIndex=position
                val view=tapLinear.getChildAt(position)
                (view as ImageView).setImageResource(R.drawable.indicator_check)
                for(i in 0 until (topNews?.size ?:0)){
                  if(i!=position){
                      val view=tapLinear.getChildAt(i)
                      (view as ImageView).setImageResource(R.drawable.indicator_normal)
                  }
                }

            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })


    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position==0 -> HEAD_TYPE
            position==itemCount-1-> FOOT_TYPE
            newsData!![position-1].id==-1L -> TIME_TYPE
            else -> ITME_NEWS_TYPE
        }
    }


    fun ondestory(){
        mDispose?.dispose()
        mDispose=null
        dbPresenter.ondestory()
    }

}

class HeadViewViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    var viewPager=itemView.find<ViewPager>(R.id.topViewPager)
    var tapLinear=itemView.find<LinearLayout>(R.id.tapLinear)

}

class DateViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var dateText=itemView.find<TextView>(R.id.textDate)
}

class NewsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var cardView=itemView.find<RelativeLayout>(R.id.cardView)
    var textTitle=itemView.find<TextView>(R.id.textTitle)
    var imageNews=itemView.find<ImageView>(R.id.imageNews)
}

class LoadMoreViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var loadingView=itemView.find<LinearLayout>(R.id.ll_foot)
    val loadingFailView=itemView.find<LinearLayout>(R.id.ll_foot_fail)
}

interface LoadMoreListener{
    fun loadingMore()
}

