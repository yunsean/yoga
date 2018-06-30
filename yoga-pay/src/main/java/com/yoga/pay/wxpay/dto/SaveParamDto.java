package com.yoga.pay.wxpay.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

public class SaveParamDto extends TenantDto {
    @NotEmpty(message = "AppID不能为空")
    private String appId;
    private String mchId;
    private String apiKey;
    private String cert;

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
