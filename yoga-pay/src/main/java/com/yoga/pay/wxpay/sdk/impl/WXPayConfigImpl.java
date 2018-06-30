package com.yoga.pay.wxpay.sdk.impl;

import com.yoga.core.utils.StrUtil;
import com.yoga.pay.wxpay.sdk.IWXPayDomain;
import com.yoga.pay.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class WXPayConfigImpl extends WXPayConfig {
    private String appId;
    private String mchId;
    private String apiKey;
    private String cert;
    private byte[] certData;


    public WXPayConfigImpl() {
    }

    public WXPayConfigImpl(String appId, String mchId, String apiKey, String cert) {
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

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public byte[] getCertData() {
        return StrUtil.base64Decode(cert);
    }

    public void setCertData(byte[] certData) {
        this.certData = certData;
    }




    public String getAppID() {
        return appId;
    }

    public String getMchID() {
        return mchId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(getCertData());
        return certBis;
    }


    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    public IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }

    public String getPrimaryDomain() {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "api2.mch.weixin.qq.com";
    }

    @Override
    public int getReportWorkerNum() {
        return 1;
    }

    @Override
    public int getReportBatchSize() {
        return 2;
    }
}
