package com.yoga.utility.qr.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class QrUploadDto extends BaseDto {

    @ApiModelProperty(value = "绑定的二维码", required = true)
    @NotBlank(message = "二维码不能为空")
    private String code;
    private boolean singleFile = false;
}
