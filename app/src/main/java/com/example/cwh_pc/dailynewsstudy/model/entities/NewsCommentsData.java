package com.example.cwh_pc.dailynewsstudy.model.entities;

public class NewsCommentsData {


    /**
     * long_comments : 1
     * popularity : 0
     * short_comments : 16
     * comments : 17
     */

    /**
     * 长评论数目
     */
    private int long_comments;
    /**
     * 点赞数
     */
    private int popularity;
    /**
     * 短评数
     */
    private int short_comments;
    /**
     * 总评论数
     */
    private int comments;

    public int getLong_comments() {
        return long_comments;
    }

    public void setLong_comments(int long_comments) {
        this.long_comments = long_comments;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(int short_comments) {
        this.short_comments = short_comments;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "NewsCommentsData{" +
                "long_comments=" + long_comments +
                ", popularity=" + popularity +
                ", short_comments=" + short_comments +
                ", comments=" + comments +
                '}';
    }
}
