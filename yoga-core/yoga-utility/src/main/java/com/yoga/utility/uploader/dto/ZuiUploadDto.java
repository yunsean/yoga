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
	@ApiModelProperty("是否调整大小")
	private boolean resize = false;
	@ApiModelProperty("宽度，resize=true必填")
	private int width;
	@ApiModelProperty("高度，resize=true必填")
	private int height;
	@ApiModelProperty("上传文件目的，用于统计显示用")
	private String purpose;
}
