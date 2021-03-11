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
public class WexinLoginDto extends BaseDto {
    @ApiModelProperty(value = "小程序CODE", required = true)
    @NotBlank(message = "请在微信小程序中使用")
    private String code;
}
