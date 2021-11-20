package com.yoga.weixinapp.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class WexinBindDto extends BaseDto {
    @ApiModelProperty(value = "站点编码或者ID")
    private String site;
    @ApiModelProperty(value = "小程序CODE", required = true)
    @NotBlank(message = "请在微信小程序中使用")
    private String code;
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "请输入用户名")
    private String username;
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "请输入密码")
    private String password;
}
