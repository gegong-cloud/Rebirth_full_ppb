package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

/**
 * 版本检查
 */
public class VersionEntity implements Serializable {
    private String is_update;
    private String download_url;

    public String getIs_update() {
        return is_update;
    }

    public void setIs_update(String is_update) {
        this.is_update = is_update;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }
}
