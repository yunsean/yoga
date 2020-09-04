package com.yoga.utility.qr.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class QrDownloadDto extends BaseDto {

    @ApiModelProperty(value = "绑定的二维码", required = true)
    @NotBlank(message = "二维码不能为空")
    private String code;
    @ApiModelProperty(value = "绑定的文件ID", required = true)
    @NotNull(message = "文件ID不能为空")
    private Long fileId;
}
