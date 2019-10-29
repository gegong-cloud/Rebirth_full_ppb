package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 直播盒子banner
 */
public class BannerInfo implements Serializable{
    private String  image;
    private String  link;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
