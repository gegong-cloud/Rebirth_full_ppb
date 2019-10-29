package com.google.android.mvp.model.back_entity;

import java.io.Serializable;

public class OpenInstallEntity {

    private AppData AppData;

    public class AppData implements Serializable{
        private UserIdEntity data;

        public UserIdEntity getData() {
            return data;
        }

        public void setData(UserIdEntity data) {
            this.data = data;
        }
    }
    public class UserIdEntity implements Serializable{
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    public OpenInstallEntity.AppData getAppData() {
        return AppData;
    }

    public void setAppData(OpenInstallEntity.AppData appData) {
        AppData = appData;
    }
}
