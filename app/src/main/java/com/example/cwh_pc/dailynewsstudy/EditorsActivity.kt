package com.example.cwh_pc.dailynewsstudy

import android.content.Intent
import android.drm.DrmStore
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.AdapterView
import com.example.cwh_pc.dailynewsstudy.adapter.ListEditorAdapter
import com.example.cwh_pc.dailynewsstudy.adapter.OnItemClick
import com.example.cwh_pc.dailynewsstudy.extension.click
import com.example.cwh_pc.dailynewsstudy.extension.toast
import com.example.cwh_pc.dailynewsstudy.model.entities.OtherThemeData
import kotlinx.android.synthetic.main.activity_aditor.*
import kotlinx.android.synthetic.main.activity_story_details.*
import org.jetbrains.anko.startActivity

class EditorsActivity:BaseActivity(){
    override fun onDestory() {
    }

    var editors:ArrayList<OtherThemeData.EditorsBean>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aditor)
        editors= intent.getSerializableExtra("editors") as ArrayList<OtherThemeData.EditorsBean>?
        initView()
    }

    private fun initView(){
        imageEdBack.click { finish() }
        if(editors!=null){
            editorRecylerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            val mAdapter=ListEditorAdapter(this,editors!!)
            mAdapter.itemClick=object :OnItemClick{
                override fun onItemClick(id: Int) {
                        val url="https://news-at.zhihu.com/api/4/editor/$id/profile-page/android"
                        this@EditorsActivity.startActivity<EditorsDetailsActivity>("url" to url)
                }

            }
            editorRecylerView.adapter=mAdapter

        }

    }


}