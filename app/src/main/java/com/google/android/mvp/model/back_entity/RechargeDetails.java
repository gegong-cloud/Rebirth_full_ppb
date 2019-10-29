package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 充值记录
 */
public class RechargeDetails implements Serializable {
    private String id;//id
    private String balance;//充值金额
    private String createtime;//充值时间
    private String status;//状态 1-新申请 2-支付成功 3-支付失败
    private String days;////有效期增加天数
    private String desc;////充值记录描述

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
