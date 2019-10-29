package com.google.android.mvp.model.upload;

import java.io.Serializable;

public class UserViewUpload implements Serializable {
    private String limit;//	是	string	每页显示的数据条数
    private String page;//	是	string	显示第几页
    private String type;//	是	string	类型 0=>全部 1=>今日 2=>7日 3=>更早

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
