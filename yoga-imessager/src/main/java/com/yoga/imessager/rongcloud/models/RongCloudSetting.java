package com.yoga.imessager.rongcloud.models;

import com.alibaba.fastjson.annotation.JSONField;

public class RongCloudSetting {
    @JSONField
    private String appCode = "";
    @JSONField
    private String appSecret = "";

    public RongCloudSetting() {

    }
    public RongCloudSetting(String appCode, String appSecret) {
        this.appCode = appCode;
        this.appSecret = appSecret;
    }

    public String getAppCode() {
        return appCode;
    }
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppSecret() {
        return appSecret;
    }
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
