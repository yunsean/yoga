package com.yoga.ewedding.counselor.dto;

import com.yoga.core.annotation.Explain;
import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

public class LoginDto extends TenantDto{

    @Explain("手机号")
    @NotEmpty(message = "请输入手机号")
    private String mobile;
    @Explain("用户密码")
    @NotEmpty(message = "请输入密码")
    private String password;
    @Explain("设备标识，比如IP或者UDID")
    private String deviceId = "";

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
