package com.google.android.mvp.model.back_entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 3.6	获取钱包首页的的温馨提示等系统基础设置
 */
public class ScrollMsg implements Serializable , CharSequence{
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

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }

    @Override
    public String toString() {
        return TextUtils.isEmpty(value)?"":value;
    }
}
