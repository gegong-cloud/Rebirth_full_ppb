package com.google.android.mvp.model.back_entity;

import java.io.Serializable;
import java.util.List;

/**
 * 云播视频
 */
public class YunboMov implements Serializable {
    private String id;
    private String name;
    private String clslist;//视频标签，多个标签之间以逗号分隔',
    private String content;//影片内容
    private String duration;//播放时长,单位秒',
    private String cover;//封面(完整域名)',
    //    private AddressEntity address;//播放地址 格式为 分辨率|线路url 多条线路之间以逗号分隔
    private String address;//播放地址 格式为 分辨率|线路url 多条线路之间以逗号分隔
    private String view_time;//	string	允许观看时间(单位秒)，为零表示没有限制 大于零表示限制时间 小于 表示不允许播放
    private String tip;//	string	提示信息
    private String type;//	string	充值类型 1=>套餐充值 2=>云播vip充值 3=>直播vip充值
    private String view_num;//
    private String has_favor;//
    private List<HeaderList> header_list;//协议头部

    private boolean editFlag = false;//是否编辑选中

    public class HeaderList implements Serializable{
        private String key;//
        private String value;//

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClslist() {
        return clslist;
    }

    public void setClslist(String clslist) {
        this.clslist = clslist;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public class AddressEntity implements Serializable {
        private List<AddressUrl> p360;//标清
        private List<AddressUrl> p480;//高清
        private List<AddressUrl> p720;//高清
        private List<AddressUrl> p1080;//高清
        private List<AddressUrl> p240;//高清

        public List<AddressUrl> getP360() {
            return p360;
        }

        public void setP360(List<AddressUrl> p360) {
            this.p360 = p360;
        }

        public List<AddressUrl> getP720() {
            return p720;
        }

        public void setP720(List<AddressUrl> p720) {
            this.p720 = p720;
        }

        public List<AddressUrl> getP480() {
            return p480;
        }

        public void setP480(List<AddressUrl> p480) {
            this.p480 = p480;
        }

        public List<AddressUrl> getP1080() {
            return p1080;
        }

        public void setP1080(List<AddressUrl> p1080) {
            this.p1080 = p1080;
        }

        public List<AddressUrl> getP240() {
            return p240;
        }

        public void setP240(List<AddressUrl> p240) {
            this.p240 = p240;
        }
    }

    public class AddressUrl implements Serializable {
        private String state;//	string	线路名称
        private String addr;//	string	播放地址
        private String ratio;//	string	播放地址


        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getView_time() {
        return view_time;
    }

    public void setView_time(String view_time) {
        this.view_time = view_time;
    }

    public String getView_num() {
        return view_num;
    }

    public void setView_num(String view_num) {
        this.view_num = view_num;
    }

    public List<HeaderList> getHeader_list() {
        return header_list;
    }

    public void setHeader_list(List<HeaderList> header_list) {
        this.header_list = header_list;
    }

    public String getHas_favor() {
        return has_favor;
    }

    public void setHas_favor(String has_favor) {
        this.has_favor = has_favor;
    }

    public boolean isEditFlag() {
        return editFlag;
    }

    public void setEditFlag(boolean editFlag) {
        this.editFlag = editFlag;
    }
}
