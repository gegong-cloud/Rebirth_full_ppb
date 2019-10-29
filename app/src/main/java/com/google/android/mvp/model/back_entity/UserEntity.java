package com.google.android.mvp.model.back_entity;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 */
public class UserEntity implements Serializable {
    private String uid;
    private String username;
    private String avatar;
    private String nickname_code;
    private String nickname;
    private String is_ever;
    private String end_time;
    private String yunbo_end_time;
    private String zhibo_end_time;
    private String is_end;
    private String token;
    private String usertype;//为用户类型 1-平台发展的用户 2-代理商发展的用户 3-代理商
    private List<AccountInfo> accountInfo;
    private Menulink menulink;
    private Menutip menutip;


    public class Menutip implements Serializable{
        private String charge_tip;//充值提示
        private String service_tip;//联系客服提示
        private String mybollet_tip;//我的钱包提示
        private String myteam_tip;//我的团队提示
        private String invate_tip;//邀请好友提示
        private String set_tip;//设置提示"

        public String getCharge_tip() {
            return charge_tip;
        }

        public void setCharge_tip(String charge_tip) {
            this.charge_tip = charge_tip;
        }

        public String getService_tip() {
            return service_tip;
        }

        public void setService_tip(String service_tip) {
            this.service_tip = service_tip;
        }

        public String getMybollet_tip() {
            return mybollet_tip;
        }

        public void setMybollet_tip(String mybollet_tip) {
            this.mybollet_tip = mybollet_tip;
        }

        public String getMyteam_tip() {
            return myteam_tip;
        }

        public void setMyteam_tip(String myteam_tip) {
            this.myteam_tip = myteam_tip;
        }

        public String getInvate_tip() {
            return invate_tip;
        }

        public void setInvate_tip(String invate_tip) {
            this.invate_tip = invate_tip;
        }

        public String getSet_tip() {
            return set_tip;
        }

        public void setSet_tip(String set_tip) {
            this.set_tip = set_tip;
        }
    }

    public class Menulink implements Serializable{
        private String howmoney_link;//如何赚钱链接
        private String doagent_link;//加入代理链接

        public String getHowmoney_link() {
            return howmoney_link;
        }

        public void setHowmoney_link(String howmoney_link) {
            this.howmoney_link = howmoney_link;
        }

        public String getDoagent_link() {
            return doagent_link;
        }

        public void setDoagent_link(String doagent_link) {
            this.doagent_link = doagent_link;
        }
    }

    public class AccountInfo implements Serializable {
        private String accounttype;//	string	状态码 1=>账号类型 2=>佣金账户 3=>金币账户
        private String balance;//	string	为账号余额
        private String gainsum;//	string	累计收益
        private String withdrowsum;//	string	累计提现

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

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<AccountInfo> getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(List<AccountInfo> accountInfo) {
        this.accountInfo = accountInfo;
    }

    public Menulink getMenulink() {
        return menulink;
    }

    public void setMenulink(Menulink menulink) {
        this.menulink = menulink;
    }

    public Menutip getMenutip() {
        return menutip;
    }

    public void setMenutip(Menutip menutip) {
        this.menutip = menutip;
    }

    public String getYunbo_end_time() {
        return yunbo_end_time;
    }

    public void setYunbo_end_time(String yunbo_end_time) {
        this.yunbo_end_time = yunbo_end_time;
    }

    public String getZhibo_end_time() {
        return zhibo_end_time;
    }

    public void setZhibo_end_time(String zhibo_end_time) {
        this.zhibo_end_time = zhibo_end_time;
    }
}
