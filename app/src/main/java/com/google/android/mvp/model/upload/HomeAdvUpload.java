package com.google.android.mvp.model.upload;

import java.io.Serializable;

/**
 * 首页广告分类
 *  轮播图-首页		slides
    启动广告		start
    轮播图-云播		yunbo
    首页底部banner	firstBottomBanner
    首页悬浮图标	firstFlout
    视频页广告		video

 */
public class HomeAdvUpload implements Serializable {
    private String code;
    private String clslist;//分类标签clslist，多个标签之间以逗号分隔
    private String cls_id;//	是	string	顶级标签id
    private String limit;//每页数量
    private String page;//当前页
    private String type;//	是	string	1=>推荐 2 =>排名 3 =>最新
    private String name;//	是	string	获取参数设置，多个代码间以逗号分隔     bolletinfo=>获取钱包首页的温馨提示     topAlert=>获取滚动公告     serviceUrl=>获取客服地址
    private String yunbo_id;//	是	string	影片id
    private String ad_id;//	是	string	广告id



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClslist() {
        return clslist;
    }

    public void setClslist(String clslist) {
        this.clslist = clslist;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCls_id() {
        return cls_id;
    }

    public void setCls_id(String cls_id) {
        this.cls_id = cls_id;
    }

    public String getYunbo_id() {
        return yunbo_id;
    }

    public void setYunbo_id(String yunbo_id) {
        this.yunbo_id = yunbo_id;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }
}
