package com.google.android.mvp.model.upload;

import java.io.Serializable;

/**
 * 登录相关参数
 */
public class LoginUpload implements Serializable{
    private String username;//是	string	用户名
    private String password;//是	string	密码
    private String referrer;//是	string	邀请码
    private String code;//是	string	短信验证码
    private String pwd;//		是	string	旧密码
    private String newpwd;//	是	string	新密码
    private String confirmpwd;//	是	string	确认密码
    private String mobile;//	是	string	手机号  发送验证码用
    private String event;//	是	string	验证码类型  register 注册 resetpwd重置密码
    private String userId;//	是	string openinstall回调时传给前端的
    private String phonetype ;//	是	1-安卓 2-ios
    private String sup_user_id;//	是	string	上级用户id     ;//	是	1-安卓 2-ios

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNewpwd() {
        return newpwd;
    }

    public void setNewpwd(String newpwd) {
        this.newpwd = newpwd;
    }

    public String getConfirmpwd() {
        return confirmpwd;
    }

    public void setConfirmpwd(String confirmpwd) {
        this.confirmpwd = confirmpwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhonetype() {
        return phonetype;
    }

    public void setPhonetype(String phonetype) {
        this.phonetype = phonetype;
    }

    public String getSup_user_id() {
        return sup_user_id;
    }

    public void setSup_user_id(String sup_user_id) {
        this.sup_user_id = sup_user_id;
    }
}
