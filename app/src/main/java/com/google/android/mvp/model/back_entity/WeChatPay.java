package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 返回微信支付对象
 */
public class WeChatPay implements Serializable {
    private String appid;//应用ID
    private String partnerid;//商户号
    private String prepayid;//预支付交易会话id
    private String noncestr;//随机字符串
    private String packagestr;//扩展字段
    private String timestamp;//时间戳
    private String sign;//签名

    private String packet_id;
    private String receive_packet_id;
    private String need_extra_pay;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackagestr() {
        return packagestr;
    }

    public void setPackagestr(String packagestr) {
        this.packagestr = packagestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPacket_id() {
        return packet_id;
    }

    public void setPacket_id(String packet_id) {
        this.packet_id = packet_id;
    }

    public String getReceive_packet_id() {
        return receive_packet_id;
    }

    public void setReceive_packet_id(String receive_packet_id) {
        this.receive_packet_id = receive_packet_id;
    }

    public String getNeed_extra_pay() {
        return need_extra_pay;
    }

    public void setNeed_extra_pay(String need_extra_pay) {
        this.need_extra_pay = need_extra_pay;
    }
}
