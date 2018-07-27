package com.example.cwh_pc.dailynewsstudy.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//侧滑菜单栏条目
public class MenuData {

    /**
     * limit : 1000
     * subscribed : []
     * others : [{"color":15007,
     * "thumbnail":"http://pic3.zhimg.com/0e71e90fd6be47630399d63c58beebfc.jpg",
     * "description":"了解自己和别人，了解彼此的欲望和局限。",
     * "id":13,
     * "name":"日常心理学"
     * }]
     */

    private int limit;
    @SerializedName("subscribed")
    private List<Menu> subscribedMenu;
    @SerializedName("others")
    private List<Menu> othersMenu;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<?> getSubscribedMenu() {
        return subscribedMenu;
    }

    public void setSubscribedMenu(List<Menu> subscribedMenu) {
        this.subscribedMenu = subscribedMenu;
    }

    public List<Menu> getOthersMenu() {
        return othersMenu;
    }

    public void setOthersMenu(List<Menu> othersMenu) {
        this.othersMenu = othersMenu;
    }

    @Override
    public String toString() {
        return "MenuData{" +
                "limit=" + limit +
                ", subscribedMenu=" + subscribedMenu +
                ", othersMenu=" + othersMenu +
                '}';
    }

    public static class Menu {
        /**
         * color : 15007
         * thumbnail : http://pic3.zhimg.com/0e71e90fd6be47630399d63c58beebfc.jpg
         * description : 了解自己和别人，了解彼此的欲望和局限。
         * id : 13
         * name : 日常心理学
         */

        private int color;
        private String thumbnail;
        private String description;
        private int id;
        private String name;

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Menu{" +
                    "color=" + color +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", description='" + description + '\'' +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
