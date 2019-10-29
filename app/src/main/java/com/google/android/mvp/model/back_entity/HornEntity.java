package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 充值小喇叭
 */
public class HornEntity implements Serializable{
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
