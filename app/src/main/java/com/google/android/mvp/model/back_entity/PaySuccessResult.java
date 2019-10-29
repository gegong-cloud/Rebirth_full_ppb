package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 支付成功结果  Data为空表示没数据或者支付还未成功，否则是支付成功。
 */
public class PaySuccessResult implements Serializable{
    private String balance;
    private String days;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
