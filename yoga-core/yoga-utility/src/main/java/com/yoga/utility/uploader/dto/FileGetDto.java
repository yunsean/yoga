package com.yoga.utility.uploader.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class FileGetDto extends BaseDto {
	@ApiModelProperty(value = "文件ID")
	@NotNull(message = "文件ID不能为空")
	private Long fileId;
}
