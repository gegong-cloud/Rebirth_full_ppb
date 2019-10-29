package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

public class ServiceAddress implements Serializable {
    private String title;
    private String value;
    private String name;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
