package com.yoga.pay.alipay.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class SaveParamDto extends TenantDto {

    @NotEmpty(message = "AppID不能为空")
    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;

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
