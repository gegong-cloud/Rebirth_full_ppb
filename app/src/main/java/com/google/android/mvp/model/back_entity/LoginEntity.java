package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 登录返回
 */
public class LoginEntity implements Serializable{
    private String  uid;
    private String  username;
    private String  avatar;
    private String  nickname_code;
    private String  nickname;
    private String  is_ever;
    private String  end_time;
    private String  is_end;
    private String  token;
    private String  usertype;//为用户类型 1-平台发展的用户 2-代理商发展的用户 3-代理商

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname_code() {
        return nickname_code;
    }

    public void setNickname_code(String nickname_code) {
        this.nickname_code = nickname_code;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIs_ever() {
        return is_ever;
    }

    public void setIs_ever(String is_ever) {
        this.is_ever = is_ever;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getIs_end() {
        return is_end;
    }

    public void setIs_end(String is_end) {
        this.is_end = is_end;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
