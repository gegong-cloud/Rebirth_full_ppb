package com.google.android.mvp.model.upload;

import java.io.Serializable;

/**
 * 支付结果
 */
public class WxPayResult implements Serializable{
    private String payStr;

    public WxPayResult(String payStr) {
        this.payStr = payStr;
    }

    public String getPayStr() {
        return payStr;
    }

    public void setPayStr(String payStr) {
        this.payStr = payStr;
    }
}
