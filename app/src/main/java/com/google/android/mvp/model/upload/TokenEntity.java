package com.google.android.mvp.model.upload;

import java.io.Serializable;

/**
 * 所有只有token的参数
 */
public class TokenEntity implements Serializable{
    private String msg_id;//	否	string	消息id
    private String money;//	否	string	yu预支付金额
    private String code;//	否	string	预支付方式
    private String cardno;//	否	string	卡号
    private String id;//	否	string	为金额列表中选中的支付金额id
    private String interfaceid;//	否	string	为选中的支付方式id
    private String chargeid;//	是	string	支付记录
    private String type;//	否	string	支付类型 1=>套餐充值 2=>云播vip充值 3=>直播vip充值


    public TokenEntity() {
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterfaceid() {
        return interfaceid;
    }

    public void setInterfaceid(String interfaceid) {
        this.interfaceid = interfaceid;
    }

    public String getChargeid() {
        return chargeid;
    }

    public void setChargeid(String chargeid) {
        this.chargeid = chargeid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
