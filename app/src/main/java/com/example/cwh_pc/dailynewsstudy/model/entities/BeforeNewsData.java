package com.example.cwh_pc.dailynewsstudy.model.entities;

import java.util.List;

public class BeforeNewsData {

    /**
     * date : 20131118
     * stories : [
     * {"images":["http://p4.zhimg.com/7b/c8/7bc8ef5947b069513c51e4b9521b5c82.jpg"],
     * "type":0,
     * "id":1747159,
     * "ga_prefix":"111822",
     * "title":"深夜食堂 · 我的张曼妮"},
     * {"images":["http://p3.zhimg.com/21/0c/210c7b63b931932fa7a1e62bf0113e7b.jpg"],
     * "type":0,"id":1858551,
     * "ga_prefix":"111822",
     * "title":"清朝皇帝上朝的时候说的是满语还是汉语？"}]
     */

    private Long date;
    private List<Story> stories;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    @Override
    public String toString() {
        return "BeforeNewsData{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                '}';
    }


}
