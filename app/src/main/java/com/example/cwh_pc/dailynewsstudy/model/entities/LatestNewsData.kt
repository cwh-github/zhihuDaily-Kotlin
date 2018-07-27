package com.example.cwh_pc.dailynewsstudy.model.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.cwh_pc.dailynewsstudy.db.converter.Converter
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *  {
date: "20140523",
stories: [
{
title: "中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）",
ga_prefix: "052321",
images: [
"http://p1.zhimg.com/45/b9/45b9f057fc1957ed2c946814342c0f02.jpg"
],
type: 0,
id: 3930445
},
...
],
top_stories: [
{
title: "商场和很多人家里，竹制家具越来越多（多图）",
image: "http://p2.zhimg.com/9a/15/9a1570bb9e5fa53ae9fb9269a56ee019.jpg",
ga_prefix: "052315",
type: 0,
id: 3930883
},
...
]
}
 */
data class LatestNewsData(@SerializedName("date") var date: Long?,
                          @SerializedName("stories") var stories: ArrayList<Story>?,
                          @SerializedName("top_stories") var top_stories: ArrayList<TopStory>?)


@Entity(tableName = "story")
@TypeConverters(Converter::class)
data class Story constructor(
        @PrimaryKey
        @SerializedName("id") var id: Long
):Serializable{
    constructor() : this(0L)

    @ColumnInfo(name = "story_title")
    @SerializedName("title")
    var title: String = ""

    @ColumnInfo(name = "ga_prefix")
    @SerializedName("ga_prefix")
    var ga_prefix: Long = 0L

    @ColumnInfo(name = "images")
    @SerializedName("images")
    var images: ArrayList<String>? = null

    @ColumnInfo(name = "type")
    @SerializedName("type")
    var type: Int = 0

    @ColumnInfo(name = "date")
    var date: Long? = 0L

    /**
     * 0 未读
     *
     * 1 已读
     */
    @ColumnInfo(name = "isRead")
    var isread: Int = 0

    /**
     * 0 为未收藏
     *
     * 1 已收藏
     */
    @ColumnInfo(name="isCollect")
    var iscollect:Int=0


    @ColumnInfo(name = "order_num")
    var olderNum: Int = 0

    override fun toString(): String {
        return "Story(id=$id, title='$title', ga_prefix=$ga_prefix, images=$images, type=$type, date=$date, isread=$isread, iscollect=$iscollect, olderNum=$olderNum)"
    }


}


@Entity(tableName = "top_story")
data class TopStory constructor(@PrimaryKey
                                @SerializedName("id") var id: Long) {

    constructor() : this(0L)

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String = ""

    @ColumnInfo(name = "image")
    @SerializedName("image")
    var image: String = ""

    @ColumnInfo(name = "ga_prefix")
    @SerializedName("ga_prefix")
    var ga_prefix: Long = 0L

    @ColumnInfo(name = "type")
    @SerializedName("type")
    var type: Int = 0

}














