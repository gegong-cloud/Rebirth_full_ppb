package com.google.android.mvp.model.back_entity;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 云播标签分类
 */
public class LabelEntity implements Serializable{

    private String id;
    private String name;
    private String rowcount;
    private String image;
    private List<LabelList> list;
    private List<HomeAdv> homeAdvs;
    private boolean isSelect =false;//标签是否选中

    public LabelEntity(){

    }
    public LabelEntity(String id,String name,boolean isSelect){
        this.id = id;
        this.name = name;
        this.isSelect = isSelect;
    }

    int type = 0;//默认0 是标签数据 1是banner数据

    public static class LabelList implements Serializable{
        private String id;
        private String name;
        private String content;
        private String duration;
        private String cover;
        private String clslist;
        private String image;
        private String has_favor;//是否收藏 1=>收藏 0=>未收藏

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClslist() {
            return clslist;
        }

        public void setClslist(String clslist) {
            this.clslist = clslist;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {

            this.cover = cover;
        }

        public String getHas_favor() {
            return has_favor;
        }

        public void setHas_favor(String has_favor) {
            this.has_favor = has_favor;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRowcount() {
        return rowcount;
    }

    public void setRowcount(String rowcount) {
        this.rowcount = rowcount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<LabelList> getList() {
        return list;
    }

    public void setList(List<LabelList> list) {
        this.list = list;
    }

    public int getType() {
        return !TextUtils.isEmpty(name)?0:1;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<HomeAdv> getHomeAdvs() {
        return homeAdvs;
    }

    public void setHomeAdvs(List<HomeAdv> homeAdvs) {
        this.homeAdvs = homeAdvs;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }


}
