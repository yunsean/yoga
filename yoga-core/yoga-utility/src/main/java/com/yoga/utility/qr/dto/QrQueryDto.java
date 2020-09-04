package com.yoga.utility.qr.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class QrQueryDto {

    @ApiModelProperty(value = "二维码编码")
    @NotBlank(message = "二维码不能为空")
    private String code;
}
