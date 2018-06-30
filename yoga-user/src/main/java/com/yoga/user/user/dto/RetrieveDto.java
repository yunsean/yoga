package com.yoga.user.user.dto;


import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RetrieveDto extends TenantDto{
    @NotNull(message = "请输入手机号")
    private String mobile;
    @NotNull(message = "请输入序号")
    private String uuid;
    @NotNull(message = "请输入验证码")
    private String captcha;
    @NotNull(message = "请输入新密码")
    @Min(value = 6, message = "新密码长度不能小于6位")
    private String newPwd;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
