package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 收支明细
 */
public class MoneyTakeDetail implements Serializable{
    private String money;
    private String createtime;
    private String remark;
    private String type;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
