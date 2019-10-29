package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 预支付结果
 */
public class PayMoneyResult implements Serializable{
    private String uid;
    private String username;//用户
    private String money;//实际支付金额
    private String url;//地址
    private String chargeid;//预支付结果id，根据id查询
    private String accountno;//预支付结果id，根据id查询
    private String payurl;//支付链接

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChargeid() {
        return chargeid;
    }

    public void setChargeid(String chargeid) {
        this.chargeid = chargeid;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getPayurl() {
        return payurl;
    }

    public void setPayurl(String payurl) {
        this.payurl = payurl;
    }
}
