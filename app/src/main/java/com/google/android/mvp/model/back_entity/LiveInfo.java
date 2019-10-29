package com.google.android.mvp.model.back_entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 直播盒子的直播信息
 */
public class LiveInfo implements Serializable{
    /**
     * 直播
     */
    private String id;
    private String title;
    private String name;
    private String img;
    private String number="0";
    private String is_badge="0";
    private String play_url;//	string	播放地址
    private String has_play;//	string	是否允许播放 1=>允许 0=>不允许
    private String view_time;//	string	允许观看时间(单位秒)，为零表示没有限制
    private String app_sys_msg;//	提示
    private String tip;//	string	提示信息
    private String type;//	string	充值类型 1=>套餐充值 2=>云播vip充值 3=>直播vip充值

    private String count="0";


    /**
     * banner
     */
    private ArrayList<BannerInfo> advList;
    private String                notice;


//    private int    type=2;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIs_badge() {
        return is_badge;
    }

    public void setIs_badge(String is_badge) {
        this.is_badge = is_badge;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<BannerInfo> getAdvList() {
        return advList;
    }

    public void setAdvList(ArrayList<BannerInfo> advList) {
        this.advList = advList;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }


    public String getHas_play() {
        return has_play;
    }

    public void setHas_play(String has_play) {
        this.has_play = has_play;
    }

    public String getView_time() {
        return view_time;
    }

    public void setView_time(String view_time) {
        this.view_time = view_time;
    }

    public String getApp_sys_msg() {
        return app_sys_msg;
    }

    public void setApp_sys_msg(String app_sys_msg) {
        this.app_sys_msg = app_sys_msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
