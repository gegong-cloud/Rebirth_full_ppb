package com.google.android.mvp.model.upload;

import java.io.Serializable;

/**
 * 标签搜索
 */
public class ClsSearch implements Serializable {
    private String limit;//	否	string	每页数量
    private String page;//	否	string	每行数量
    private String q;//	是	string	搜索内容

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
