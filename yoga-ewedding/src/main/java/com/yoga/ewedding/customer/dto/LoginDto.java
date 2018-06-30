package com.yoga.ewedding.customer.dto;

import com.yoga.core.annotation.Explain;
import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

public class LoginDto extends TenantDto{

    @Explain("用户名或者手机号")
    @NotEmpty(message = "请输入用户名")
    private String username;
    @Explain("用户密码")
    @NotEmpty(message = "请输入密码")
    private String password;
    @Explain("设备标识，比如IP或者UDID")
    private String deviceId = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
