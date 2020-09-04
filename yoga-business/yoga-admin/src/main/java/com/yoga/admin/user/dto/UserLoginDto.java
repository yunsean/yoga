package com.yoga.admin.user.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginDto extends BaseDto{

    @ApiModelProperty(value = "用户名或者手机号", required = true)
    @NotEmpty(message = "请输入用户名")
    private String username;
    @ApiModelProperty(value = "用户密码", required = true)
    @NotEmpty(message = "请输入密码")
    private String password;
    @ApiModelProperty(value = "设备标识，比如IP或者UDID", required = true)
    private String deviceId = "";
}
