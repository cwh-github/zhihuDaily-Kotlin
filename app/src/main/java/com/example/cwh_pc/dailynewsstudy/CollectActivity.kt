package com.example.cwh_pc.dailynewsstudy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.cwh_pc.dailynewsstudy.adapter.CollectionAdapter
import com.example.cwh_pc.dailynewsstudy.extension.click
import com.example.cwh_pc.dailynewsstudy.model.entities.Story
import com.example.cwh_pc.dailynewsstudy.presenter.StoryDBPresenter
import kotlinx.android.synthetic.main.activity_collect.*

class CollectActivity : AppCompatActivity(),StoryDBPresenter.OnQueryCollectListener {

    override fun onQuerySuccess(stories: List<Story>) {
        if(stories==null || stories.isEmpty()){
            textNoData.visibility=View.VISIBLE
        }else{
            collectRecylerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            collectRecylerView.adapter=CollectionAdapter(stories,this)
        }
    }

    lateinit var dbPresenter: StoryDBPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)
        dbPresenter= StoryDBPresenter(this,this)
        dbPresenter.collectListener=this
        dbPresenter.getAllCollectStory()

        imageCollectBack.click {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbPresenter?.ondestory()
    }
}
