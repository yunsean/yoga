package com.yoga.admin.setting.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class SettingGetDto extends BaseDto {

	@ApiModelProperty(value = "所属模块", required = true)
	@NotNull(message = "未指定设置模块")
	private String module;
	@ApiModelProperty(value = "参数名称", required = true)
	@NotNull(message = "未指定设置参数名称")
	private String key;
}
