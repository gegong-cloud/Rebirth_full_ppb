package com.google.android.mvp.model.back_entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 直播盒子banner
 */
public class BannerEntity implements Serializable{
    private ArrayList<BannerInfo> advList;
    private String                notice;

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
}
