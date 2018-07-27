package com.example.cwh_pc.dailynewsstudy.model.entities;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OtherThemeData {

    /**
     * stories : [{"type":0,"id":7097426,"title":"人们在虚拟生活中投入的精力是否对现实生活的人际关系有积极意义？"},
     * {"images":["http://pic1.zhimg.com/56d1d1202077c7b5b0e48e3b7d3ebb60_t.jpg"],
     * "type":0,"id":7101963,
     * "title":"写给想成为心理咨询师的学生同仁"}]
     * description : 了解自己和别人，了解彼此的欲望和局限。
     * background : http://pic2.zhimg.com/71c8bcd3d99958de45ed87b8fc213224.jpg
     * color : 15007
     * name : 日常心理学
     * image : http://pic4.zhimg.com/60b69ef145a472f2c6b5302453f95eaa.jpg
     * editors : [{"url":"http://www.zhihu.com/people/moheng-esther","bio":"树上的女爵","id":79,"avatar":"http://pic1.zhimg.com/0a6456810_m.jpg","name":"刘柯"}]
     * image_source :
     */

    private String description;
    private String background;
    private int color;
    private String name;
    private String image;
    private String image_source;
    public List<StoriesBean> stories;
    private ArrayList<EditorsBean> editors;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public ArrayList<EditorsBean> getEditors() {
        return editors;
    }

    public void setEditors(ArrayList<EditorsBean> editors) {
        this.editors = editors;
    }

    @Override
    public String toString() {
        return "OtherThemeData{" +
                "description='" + description + '\'' +
                ", background='" + background + '\'' +
                ", color=" + color +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", image_source='" + image_source + '\'' +
                ", stories=" + stories +
                ", editors=" + editors +
                '}';
    }

    public static class StoriesBean {
        /**
         * type : 0
         * id : 7097426
         * title : 人们在虚拟生活中投入的精力是否对现实生活的人际关系有积极意义？
         * images : ["http://pic1.zhimg.com/56d1d1202077c7b5b0e48e3b7d3ebb60_t.jpg"]
         */

        private int type;
        private int id;
        private String title;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        @Override
        public String toString() {
            return "StoriesBean{" +
                    "type=" + type +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    ", images=" + images +
                    '}';
        }
    }

    public static class EditorsBean implements Serializable{
        /**
         * url : http://www.zhihu.com/people/moheng-esther
         * bio : 树上的女爵
         * id : 79
         * avatar : http://pic1.zhimg.com/0a6456810_m.jpg
         * name : 刘柯
         */

        private String url;
        private String bio;
        private int id;
        private String avatar;
        private String name;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "EditorsBean{" +
                    "url='" + url + '\'' +
                    ", bio='" + bio + '\'' +
                    ", id=" + id +
                    ", avatar='" + avatar + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
