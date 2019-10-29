package com.google.android.mvp.model.upload;

import java.io.Serializable;

/**
 * 提现
 */
public class WithDrowAppUpload implements Serializable{
    private String money;//money为提现金额
    private String wxcode ;//wxcode 微信号',
    private String alipaycode ;//alipaycode 支付宝账号',
    private String bankname ;//bankname 银行名称',
    private String accountno  ;//accountno 银行卡号',

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getWxcode() {
        return wxcode;
    }

    public void setWxcode(String wxcode) {
        this.wxcode = wxcode;
    }

    public String getAlipaycode() {
        return alipaycode;
    }

    public void setAlipaycode(String alipaycode) {
        this.alipaycode = alipaycode;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }
}
