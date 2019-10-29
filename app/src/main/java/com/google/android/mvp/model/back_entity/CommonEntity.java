package com.google.android.mvp.model.back_entity;


import android.text.TextUtils;

import java.io.Serializable;

/**
 * 共用的实体类，验证是否操作成功
 */
public class CommonEntity<T> implements Serializable{


    private String code;
    private String msg;
    private ErrorMsg error;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorMsg getError() {
        return error==null?new ErrorMsg():error;
    }

    public void setError(ErrorMsg error) {
        this.error = error;
    }

    public  class ErrorMsg implements Serializable {
        private  String msg;
        private String url;

        public String getMsg() {
            return !TextUtils.isEmpty(msg)?msg:"";
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getUrl() {
            return !TextUtils.isEmpty(url)?url:"";
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
