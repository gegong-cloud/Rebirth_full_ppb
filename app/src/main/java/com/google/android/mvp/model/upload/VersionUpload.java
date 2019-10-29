package com.google.android.mvp.model.upload;

import java.io.Serializable;

public class VersionUpload implements Serializable {
    private String version;
    private String type;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
