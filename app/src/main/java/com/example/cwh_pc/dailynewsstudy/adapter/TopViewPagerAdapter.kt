package com.example.cwh_pc.dailynewsstudy.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.cwh_pc.dailynewsstudy.R
import com.example.cwh_pc.dailynewsstudy.extension.click
import com.example.cwh_pc.dailynewsstudy.extension.loadImage
import com.example.cwh_pc.dailynewsstudy.model.entities.TopStory

class TopViewPagerAdapter(var topNews:ArrayList<TopStory>?,var context:Context): PagerAdapter() {
    var listener:ViewPagerClickListener?=null
    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view==`object`
    }

    override fun getCount(): Int {
      return topNews?.size ?: 0
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val view=View.inflate(context, R.layout.top_head_item,null)
        val imageView=view.find<ImageView>(R.id.homeTopImage)
        val textTitle=view.find<TextView>(R.id.topNewsTitle)
        loadImage(context,imageView, topNews?.get(position)?.image)
        textTitle.text=topNews?.get(position)?.title
        container?.addView(view)
        view.click {
            listener?.onItemClick(topNews?.get(position)?.id)
        }
        return view
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View)
    }

}

interface ViewPagerClickListener{
    fun onItemClick(id: Long?)
}