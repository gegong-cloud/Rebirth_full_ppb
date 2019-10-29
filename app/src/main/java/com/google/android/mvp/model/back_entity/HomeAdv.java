package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 首页广告
 *
 * 轮播图-首页		slides
 启动广告		start
 轮播图-云播		yunbo
 首页底部banner	firstBottomBanner
 首页悬浮图标	firstFlout
 视频页广告		video
 *
 */
public class HomeAdv implements Serializable{
    private String id;
    private String name;
    private String link; //外部链接地址 或者 内部模块代码
    private String image;
    private String position_id;
    private String position_name;
    private String code;
    private String location_url;//1-H5外部链接 2-H5内部链接3-内部模块（编号）

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPosition_id() {
        return position_id;
    }

    public void setPosition_id(String position_id) {
        this.position_id = position_id;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocation_url() {
        return location_url;
    }

    public void setLocation_url(String location_url) {
        this.location_url = location_url;
    }
}
