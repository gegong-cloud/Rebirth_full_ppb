package com.google.android.mvp.model.back_entity;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 首页影视
 */
public class HomeVipMov implements Serializable {

    private String name;
    private List<HomeVipMovList> list;
    private List<HomeAdv> bottomAdv;
    private int type = 0;

    public class HomeVipMovList implements Serializable {
        private String id;
        private String title;
        private String image;
        private String link;
        private String sort;
        private String view_num;//播放次数
        private String tip;//显示时间、标签等
        private String isinner;//区分跳转
        private String iscommit;//iscommit=1的是推荐服务
        private String has_favor;//是否收藏 1=>收藏 0=>未收藏
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getIscommit() {
            return iscommit;
        }

        public void setIscommit(String iscommit) {
            this.iscommit = iscommit;
        }

        public String getIsinner() {
            return isinner;
        }

        public void setIsinner(String isinner) {
            this.isinner = isinner;
        }

        public String getView_num() {
            return view_num;
        }

        public void setView_num(String view_num) {
            this.view_num = view_num;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public String getHas_favor() {
            return has_favor;
        }

        public void setHas_favor(String has_favor) {
            this.has_favor = has_favor;
        }
    }

    public int getType() {
        return !TextUtils.isEmpty(name)?0:1;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<HomeAdv> getBottomAdv() {
        return bottomAdv;
    }

    public void setBottomAdv(List<HomeAdv> bottomAdv) {
        this.bottomAdv = bottomAdv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HomeVipMovList> getList() {
        return list;
    }

    public void setList(List<HomeVipMovList> list) {
        this.list = list;
    }
}
