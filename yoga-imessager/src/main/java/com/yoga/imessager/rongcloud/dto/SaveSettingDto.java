package com.yoga.imessager.rongcloud.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class SaveSettingDto extends TenantDto {

    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
    @NotEmpty(message = "请输入APP Code")
    private String appCode;
    @NotEmpty(message = "请输入APP Secret")
    private String appSecret;

    public Long getTenantId() {
        return tenantId;
    }
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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
