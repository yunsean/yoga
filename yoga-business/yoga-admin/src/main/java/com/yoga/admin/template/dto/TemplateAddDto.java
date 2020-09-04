package com.yoga.admin.template.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class TemplateAddDto extends BaseDto {

	@ApiModelProperty(value = "模板名称，100字符以内", required = true)
	@NotNull(message = "模板名称不能为空")
	@Size(min = 1, max = 100, message = "模板名称长度只能在1-100个字符之间")
	private String name;
	@ApiModelProperty(value = "模板描述，512字符以内")
	@Size(max = 512, message = "模板描述长度不能超过512个字符")
	private String remark;
}
