package com.google.android.mvp.model.back_entity;

import java.io.Serializable;
import java.util.List;

/**
 * 4.1	用户账户明细
 */
public class AccountDetail implements Serializable{
    private String uid;
    private String username;
    private String avatar;
    private String nickname_code;
    private String nickname;
    private String is_ever;
    private String end_time;
    private String is_end;
    private List<AccountInfo> accountInfo;

    public class AccountInfo implements Serializable{
        private String accounttype;//账号类型 1-现金账户 2-佣金账户 3-金币账户
        private String balance;//为账号余额
        private String gainsum;//累计收益
        private String withdrowsum;//累计提现

        public String getAccounttype() {
            return accounttype;
        }

        public void setAccounttype(String accounttype) {
            this.accounttype = accounttype;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getGainsum() {
            return gainsum;
        }

        public void setGainsum(String gainsum) {
            this.gainsum = gainsum;
        }

        public String getWithdrowsum() {
            return withdrowsum;
        }

        public void setWithdrowsum(String withdrowsum) {
            this.withdrowsum = withdrowsum;
        }
    }

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname_code() {
        return nickname_code;
    }

    public void setNickname_code(String nickname_code) {
        this.nickname_code = nickname_code;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIs_ever() {
        return is_ever;
    }

    public void setIs_ever(String is_ever) {
        this.is_ever = is_ever;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getIs_end() {
        return is_end;
    }

    public void setIs_end(String is_end) {
        this.is_end = is_end;
    }

    public List<AccountInfo> getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(List<AccountInfo> accountInfo) {
        this.accountInfo = accountInfo;
    }
}
