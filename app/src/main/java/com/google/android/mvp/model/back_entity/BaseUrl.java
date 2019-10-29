package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

public class BaseUrl implements Serializable{
    private String url;

    public String getUrl() {
        return url.contains("http")?("http://"+url):url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
