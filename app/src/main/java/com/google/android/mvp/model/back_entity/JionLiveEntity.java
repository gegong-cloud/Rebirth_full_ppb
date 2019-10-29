package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 观众加入直播，校验会员是否过期
 */
public class JionLiveEntity implements Serializable{
    private String  app_sys_msg;
    private String link;

    public String getApp_sys_msg() {
        return app_sys_msg;
    }

    public void setApp_sys_msg(String app_sys_msg) {
        this.app_sys_msg = app_sys_msg;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
