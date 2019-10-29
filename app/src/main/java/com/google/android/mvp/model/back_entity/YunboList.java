package com.google.android.mvp.model.back_entity;

import java.io.Serializable;
import java.util.List;

/**
 * 云播列表
 */
public class YunboList implements Serializable {
    private String name;//	string	影片名字
    private List<YunboMov> list;//	string	影片名字

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<YunboMov> getList() {
        return list;
    }

    public void setList(List<YunboMov> list) {
        this.list = list;
    }
}
