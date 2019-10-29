package com.google.android.mvp.model.back_entity;

import java.io.Serializable;
import java.util.List;

/**
 * vip 观影地址
 */
public class VIPVideoEntity implements Serializable{
    private String tv_vip_link;//	vip影院观看地址
    //public String  tv_vip_jx="http://yun.baiyug.cn/vip/?url=";//破解地址
    private List<String> tv_vip_interface;

    public String getTv_vip_link() {
        return tv_vip_link;
    }

    public void setTv_vip_link(String tv_vip_link) {
        this.tv_vip_link = tv_vip_link;
    }

    public List<String> getTv_vip_interface() {
        return tv_vip_interface;
    }

    public void setTv_vip_interface(List<String> tv_vip_interface) {
        this.tv_vip_interface = tv_vip_interface;
    }
}
