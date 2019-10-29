package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 推广链接--用户邀请
 */
public class UserInvite implements Serializable{
    private String intive_code;
    private String android_apk_version;
    private String android_download_url;
    private String ios_ipa_version;
    private String ios_download_url;
    private String invateInfo;
    private String erweimaUrl;
    private String now;
    private String invateUrl;
    private String invateTip;

    public String getIntive_code() {
        return intive_code;
    }

    public void setIntive_code(String intive_code) {
        this.intive_code = intive_code;
    }

    public String getAndroid_apk_version() {
        return android_apk_version;
    }

    public void setAndroid_apk_version(String android_apk_version) {
        this.android_apk_version = android_apk_version;
    }

    public String getAndroid_download_url() {
        return android_download_url;
    }

    public void setAndroid_download_url(String android_download_url) {
        this.android_download_url = android_download_url;
    }

    public String getIos_ipa_version() {
        return ios_ipa_version;
    }

    public void setIos_ipa_version(String ios_ipa_version) {
        this.ios_ipa_version = ios_ipa_version;
    }

    public String getIos_download_url() {
        return ios_download_url;
    }

    public void setIos_download_url(String ios_download_url) {
        this.ios_download_url = ios_download_url;
    }

    public String getInvateInfo() {
        return invateInfo;
    }

    public void setInvateInfo(String invateInfo) {
        this.invateInfo = invateInfo;
    }

    public String getErweimaUrl() {
        return erweimaUrl;
    }

    public void setErweimaUrl(String erweimaUrl) {
        this.erweimaUrl = erweimaUrl;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }


    public String getInvateUrl() {
        return invateUrl;
    }

    public void setInvateUrl(String invateUrl) {
        this.invateUrl = invateUrl;
    }

    public String getInvateTip() {
        return invateTip;
    }

    public void setInvateTip(String invateTip) {
        this.invateTip = invateTip;
    }
}
