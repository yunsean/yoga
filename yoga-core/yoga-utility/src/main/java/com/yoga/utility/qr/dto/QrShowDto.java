package com.yoga.utility.qr.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ApiModel("二维码信息")
public class QrShowDto extends BaseDto {

    @NotBlank(message = "QR不能为空")
    @Size(min = 40, max = 40, message = "无效的二维码")
    @ApiModelProperty(value = "二维码编码", required = true)
    private String code;
    @NotBlank(message = "文档地址不能为空")
    @ApiModelProperty(value = "绑定的文档地址", required = true)
    private String url;
    @ApiModelProperty(value = "设置此参数后，后台将代为访问URL后返回内容，默认值为false")
    private boolean transfer = false;
}
