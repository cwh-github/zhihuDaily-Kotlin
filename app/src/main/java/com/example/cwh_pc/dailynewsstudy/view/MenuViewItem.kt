package com.example.cwh_pc.dailynewsstudy.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.example.cwh_pc.dailynewsstudy.R
import org.jetbrains.anko.*


object MenuViewItem {

    /**
     * 创建主页Item
     */
    fun createMenuHomeItem(context: Context): View {
        return with(context) {
            relativeLayout {
                lparams(width = matchParent, height = dip(48))
                backgroundColor = Color.WHITE
                textView {
                    id = R.id.text
                    text="首页"
                    textColor=R.color.colorPrimary
                    textSize=16f
                    gravity=Gravity.CENTER_VERTICAL or Gravity.LEFT
                    setPadding(dip(16),0,0,0)
                    setCompoundDrawables(getDrawable(context,R.mipmap.menu_home),null,null,null)
                    compoundDrawablePadding=dip(16)
                }.lparams(width= matchParent,height = matchParent)
            }
        }
    }

    /**
     * 创建其他Item
     */
    fun createMenuItem(context: Context):View{
        return with(context){
            relativeLayout {
                lparams(width = matchParent, height = dip(48))
                backgroundColor = Color.WHITE
                textView {
                    id = R.id.text
                    text="日常心理学"
                    textColor=R.color.splash_bg_color
                    textSize=16f
                    gravity=Gravity.CENTER_VERTICAL or Gravity.LEFT
                    setPadding(dip(16),0,dip(16),0)
                    setCompoundDrawables(null,null,getDrawable(context,R.mipmap.home_arrow),null)
                }.lparams(width= matchParent,height = matchParent)
            }
        }
    }

    private fun getDrawable(context: Context, id: Int): Drawable {
        val drawable = context.resources.getDrawable(id)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        return drawable
    }
}