package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 我的团队
 */
public class MyTeam implements Serializable{
    private String username;
    private String end_time;
    private String active;//active=1表示已登录，0表示未登录

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
