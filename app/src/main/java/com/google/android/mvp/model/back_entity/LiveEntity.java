package com.google.android.mvp.model.back_entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 直播盒子列表请求结果
 */
public class LiveEntity implements Serializable {
    private String count;
    private String title;
    private ArrayList<LiveInfo> lists;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<LiveInfo> getLists() {
        return lists;
    }

    public void setLists(ArrayList<LiveInfo> lists) {
        this.lists = lists;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
