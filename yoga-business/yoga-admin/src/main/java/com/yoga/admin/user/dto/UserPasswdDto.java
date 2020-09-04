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
public class UserPasswdDto extends BaseDto {
    @ApiModelProperty(value = "当前密码", required = true)
    @NotEmpty(message = "请输入旧密码")
    private String oldPwd;
    @ApiModelProperty(value = "新密码", required = true)
    @NotEmpty(message = "请输入新密码")
    private String newPwd;
}
