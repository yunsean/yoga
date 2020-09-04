package com.yoga.admin.user.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UserCaptchaDto extends BaseDto {
    @ApiModelProperty(value = "手机号", required = true)
    @NotNull(message = "请输入手机号")
    private String mobile;
}
