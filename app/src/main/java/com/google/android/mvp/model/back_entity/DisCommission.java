package com.google.android.mvp.model.back_entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分销提成
 */
public class DisCommission implements Serializable{
    private String usernum;//Usernum为充值的用户数
    private String money;//money为总充值金额
    private List<DisList> list;

    public class DisList implements Serializable{
        private String id;//
        private String user_id;//
        private String subuser_id;//
        private String bonustype;//
        private String charge_id;//
        private String level;//
        private String balance;//
        private String money;//
        private String beforemoney;//
        private String aftermoney;//
        private String createtime;//
        private String remark;//
        private String username;//

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getSubuser_id() {
            return subuser_id;
        }

        public void setSubuser_id(String subuser_id) {
            this.subuser_id = subuser_id;
        }

        public String getBonustype() {
            return bonustype;
        }

        public void setBonustype(String bonustype) {
            this.bonustype = bonustype;
        }

        public String getCharge_id() {
            return charge_id;
        }

        public void setCharge_id(String charge_id) {
            this.charge_id = charge_id;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getBeforemoney() {
            return beforemoney;
        }

        public void setBeforemoney(String beforemoney) {
            this.beforemoney = beforemoney;
        }

        public String getAftermoney() {
            return aftermoney;
        }

        public void setAftermoney(String aftermoney) {
            this.aftermoney = aftermoney;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public String getUsernum() {
        return usernum;
    }

    public void setUsernum(String usernum) {
        this.usernum = usernum;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<DisList> getList() {
        return list;
    }

    public void setList(List<DisList> list) {
        this.list = list;
    }
}
