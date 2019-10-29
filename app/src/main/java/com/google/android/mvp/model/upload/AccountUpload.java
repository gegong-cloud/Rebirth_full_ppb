package com.google.android.mvp.model.upload;

import java.io.Serializable;

/**
 * 更新用户收款账号
 */
public class AccountUpload implements Serializable{
    private String wxcode;//微信号
    private String alipaycode;//支付宝账号',
    private String bankname;//银行名称',
    private String accountno;//银行卡号',
    private String accountname;//持卡人姓名',
    private String accountphone;//预留手机号',




    private String limit ;//每页显示的数据条数',
    private String page ;//显示第几页',



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

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getAccountphone() {
        return accountphone;
    }

    public void setAccountphone(String accountphone) {
        this.accountphone = accountphone;
    }

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
}
