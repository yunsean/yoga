package com.yoga.pay.alipay.model;

import com.alibaba.fastjson.annotation.JSONField;

public class AlipayParam {
    @JSONField
    private String appId;
    @JSONField
    private String appPrivateKey;
    @JSONField
    private String alipayPublicKey;

    public AlipayParam() {
    }
    public AlipayParam(String appId, String appPrivateKey, String alipayPublicKey) {
        this.appId = appId;
        this.appPrivateKey = appPrivateKey;
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppPrivateKey() {
        return appPrivateKey;
    }
    public void setAppPrivateKey(String appPrivateKey) {
        this.appPrivateKey = appPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }
    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }
}
