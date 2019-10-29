package com.google.android.mvp.model.upload;

import java.io.Serializable;

/**
 * 获取直播平台房间
 */
public class LiveUpload implements Serializable {
    private String name;
    private String zhibo_id;
    private String vip_cate_id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZhibo_id() {
        return zhibo_id;
    }

    public void setZhibo_id(String zhibo_id) {
        this.zhibo_id = zhibo_id;
    }

    public String getVip_cate_id() {
        return vip_cate_id;
    }

    public void setVip_cate_id(String vip_cate_id) {
        this.vip_cate_id = vip_cate_id;
    }
}
