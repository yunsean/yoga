package com.yoga.admin.user.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLogonDto extends BaseDto {

    @ApiModelProperty(value = "用户名，如果为空，则mobile不能为空")
    private String username;
    @ApiModelProperty(value = "注册密码")
    private String password;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "手机验证码对应的盐值，如果开启短信验证则不能为空")
    private String uuid;
    @ApiModelProperty(value = "手机验证码，如果开启短信验证则不能为空")
    private String captcha;
    @ApiModelProperty(value = "用户头像")
    private String avatar;
    @ApiModelProperty(value = "用户昵称")
    private String nickname;
    @ApiModelProperty(value = "电子邮箱")
    private String email;
}
