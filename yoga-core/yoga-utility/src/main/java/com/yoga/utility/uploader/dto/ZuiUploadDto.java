package com.yoga.utility.uploader.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ZuiUploadDto extends BaseDto {
	@ApiModelProperty(value = "是否调整大小")
	private boolean resize = false;
	@ApiModelProperty(value = "宽度，resize=true必填")
	private int width;
	@ApiModelProperty(value = "高度，resize=true必填")
	private int height;
	@ApiModelProperty(value = "上传文件目的，用于统计显示用")
	private String purpose;
}
