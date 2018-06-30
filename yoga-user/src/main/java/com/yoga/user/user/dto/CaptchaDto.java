package com.yoga.user.user.dto;


import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class CaptchaDto extends TenantDto {
    @NotNull(message = "请输入手机号")
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
