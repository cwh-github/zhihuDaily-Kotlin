package com.example.cwh_pc.dailynewsstudy.model.entities;

import java.util.List;

public class NewsShortComments {

    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "NewsShortComments{" +
                "comments=" + comments +
                '}';
    }

    public static class CommentsBean {
        /**
         * author : TuoTuo的亲爹
         * content : 第一次也是吃了牛肉…吃完以后，人家很客气的问我还需要么…我也很客气地再来了一盒鸡肉的。。。。可惜后来专门坐那个航班，居然换成茄子饭了?
         * avatar : http://pic1.zhimg.com/8949bb2f30ed5789857accd489644234_im.jpg
         * time : 1413859600
         * reply_to : {"content":"我每次都不假思索选了牛肉，然后就深深的后悔没有试过一次鸡肉，到下一次又情不自禁选了牛肉，周而复始循环往复-_-#","status":0,"id":551969,"author":"怒放的腋毛"}
         * id : 552762
         * likes : 0
         */

        private String author;
        private String content;
        private String avatar;
        private int time;
        private ReplyToBean reply_to;
        private int id;
        private int likes;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public ReplyToBean getReply_to() {
            return reply_to;
        }

        public void setReply_to(ReplyToBean reply_to) {
            this.reply_to = reply_to;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        @Override
        public String toString() {
            return "CommentsBean{" +
                    "author='" + author + '\'' +
                    ", content='" + content + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", time=" + time +
                    ", reply_to=" + reply_to +
                    ", id=" + id +
                    ", likes=" + likes +
                    '}';
        }

        public static class ReplyToBean {
            /**
             * content : 我每次都不假思索选了牛肉，然后就深深的后悔没有试过一次鸡肉，到下一次又情不自禁选了牛肉，周而复始循环往复-_-#
             * status : 0
             * id : 551969
             * author : 怒放的腋毛
             */

            private String content;
            private int status;
            private int id;
            private String author;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            @Override
            public String toString() {
                return "ReplyToBean{" +
                        "content='" + content + '\'' +
                        ", status=" + status +
                        ", id=" + id +
                        ", author='" + author + '\'' +
                        '}';
            }
        }
    }
}
