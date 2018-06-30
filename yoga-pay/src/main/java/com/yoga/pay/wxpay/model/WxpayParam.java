package com.yoga.pay.wxpay.model;

import com.alibaba.fastjson.annotation.JSONField;

public class WxpayParam {
    @JSONField
    private String appId;
    @JSONField
    private String mchId;
    @JSONField
    private String apiKey;
    @JSONField
    private String cert;

    public WxpayParam() {
    }

    public WxpayParam(String appId, String mchId, String apiKey, String cert) {
        this.appId = appId;
        this.mchId = mchId;
        this.apiKey = apiKey;
        this.cert = cert;
    }



    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }
}
