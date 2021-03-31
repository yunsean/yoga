package com.yoga.utility.qr.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChartDto {

    @ApiModelProperty(value = "需要生成图片的二维码", required = true)
    @NotBlank(message = "二维码不能为空")
    private String code;
    @ApiModelProperty(value = "二维码宽度，默认值512px")
    private int width = 512;
    @ApiModelProperty(value = "二维码高度，默认512px")
    private int height = 512;
    @ApiModelProperty(value = "二维码颜色")
    private long color = 0xff000000;
}
