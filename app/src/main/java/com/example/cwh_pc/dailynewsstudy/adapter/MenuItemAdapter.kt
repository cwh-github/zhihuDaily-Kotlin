package com.example.cwh_pc.dailynewsstudy.adapter

import android.graphics.Color
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.cwh_pc.dailynewsstudy.R
import com.example.cwh_pc.dailynewsstudy.extension.click
import com.example.cwh_pc.dailynewsstudy.model.entities.MenuData
import com.example.cwh_pc.dailynewsstudy.view.MenuViewItem
import org.jetbrains.anko.backgroundColor

class MenuItemAdapter(var menuData: MutableList<MenuData.Menu>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val HEAD_TYPE = 0
    val HOME_TYPE = 1
    val MENU_TYPE = 2

    var menuClickListener:MenuItemClickListener?=null
    var cliclItem:View?=null
    var downTextView:TextView?=null


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEAD_TYPE -> {
                val view = LayoutInflater.from(parent?.context).inflate(R.layout.menu_head_layout, parent, false)
                HeadViewHolder(view)
            }
            HOME_TYPE -> MenuViewHolder(MenuViewItem.createMenuHomeItem(parent?.context!!), true)
            else -> MenuViewHolder(MenuViewItem.createMenuItem(parent?.context!!), false)
        }
    }

    override fun getItemCount(): Int {
        return (menuData?.size ?: 0) + 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEAD_TYPE
            1 -> HOME_TYPE
            else -> MENU_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            HEAD_TYPE -> {
                val menuHolder = holder as HeadViewHolder
                downTextView=menuHolder.mTvDownLoad
                menuHolder.mImageUser.click {
                    menuClickListener?.onOtherClick(it)
                }

                menuHolder.mTvName.click{
                    menuClickListener?.onOtherClick(it)
                }

                menuHolder.mTvFavorites.click {
                    menuClickListener?.onCollectionClick()
                }

                menuHolder.mTvDownLoad.click {
                    menuClickListener?.onDownLoadClick(it)
                }
            }
            HOME_TYPE -> {
                val homeHolder = holder as MenuViewHolder
                cliclItem=homeHolder.itemView
                homeHolder.itemView.click {
                    cliclItem?.setBackgroundColor(Color.WHITE)
                    it.setBackgroundColor(Color.parseColor("#f0f0f0"))
                    cliclItem=it
                    menuClickListener?.onHomeMenuClick()
                }
            }
            else -> {
                val homeHolder = holder as MenuViewHolder
                val sourceData=menuData!![position-2]
                homeHolder.text.text = sourceData.name
                homeHolder.itemView.click {
                    cliclItem?.setBackgroundColor(Color.WHITE)
                    it.setBackgroundColor(Color.parseColor("#f0f0f0"))
                    cliclItem=it
                    menuClickListener?.onItemClick(sourceData.id,sourceData.name)
                }
            }
        }
    }

}

class MenuViewHolder(itemView: View, var isClick: Boolean) : RecyclerView.ViewHolder(itemView) {
    var text: TextView = itemView.find(R.id.text)

    init {
        if (isClick) {
            itemView.setBackgroundColor(Color.parseColor("#f0f0f0"))
        } else {
            itemView.backgroundColor = android.R.color.white
        }
    }
}

class HeadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mImageUser: ImageView = itemView.find(R.id.mUserImg)
    var mTvName: TextView = itemView.find(R.id.mTvUserName)
    var mTvFavorites = itemView.find<TextView>(R.id.mTvCollection)
    var mTvDownLoad = itemView.find<TextView>(R.id.mTvDownLoad)

}

fun <T : View> View.find(@IdRes id: Int): T {
    return this.findViewById(id)
}

interface MenuItemClickListener{
    fun onHomeMenuClick()

    fun onItemClick(id: Int, name: String)

    fun onCollectionClick()

    fun onDownLoadClick(it: View)

    fun onOtherClick(view:View)
}