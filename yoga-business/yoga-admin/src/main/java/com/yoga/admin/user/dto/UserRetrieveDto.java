package com.yoga.admin.user.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UserRetrieveDto extends BaseDto{
    @ApiModelProperty(value = "手机号", required = true)
    @NotNull(message = "请输入手机号")
    private String mobile;
    @ApiModelProperty(value = "手机验证码对应的盐值", required = true)
    @NotNull(message = "请输入序号")
    private String uuid;
    @ApiModelProperty(value = "手机验证码", required = true)
    @NotNull(message = "请输入验证码")
    private String captcha;
    @ApiModelProperty(value = "新密码，不能小于6位", required = true)
    @NotNull(message = "请输入新密码")
    @Min(value = 6, message = "新密码长度不能小于6位")
    private String newPwd;
}
