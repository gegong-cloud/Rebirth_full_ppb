package com.google.android.mvp.model.upload;

import java.io.Serializable;

public class UserDeleteUpload implements Serializable {
    private  String yunbo_id;//	是	string	云播ID数组
    private  String[] yunbo_ids;//	是	string	云播ID数组

    public String[] getYunbo_ids() {
        return yunbo_ids;
    }

    public void setYunbo_ids(String[] yunbo_ids) {
        this.yunbo_ids = yunbo_ids;
    }

    public String getYunbo_id() {
        return yunbo_id;
    }

    public void setYunbo_id(String yunbo_id) {
        this.yunbo_id = yunbo_id;
    }
}
