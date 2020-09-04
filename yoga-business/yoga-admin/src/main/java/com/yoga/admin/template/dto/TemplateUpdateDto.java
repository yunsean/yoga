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
public class TemplateUpdateDto extends BaseDto {

	@ApiModelProperty(value = "模板ID", required = true)
	@NotNull(message = "模板ID不能为空")
	private Long id;
	@ApiModelProperty(value = "新的模板名称，不为空则更新")
	@NotNull(message = "模板名称不能为空")
	@Size(min = 1, max = 100, message = "模板名称长度只能在1-100个字符之间")
	private String name;
	@ApiModelProperty(value = "新的模板描述，传值则更新")
	@Size(max = 512, message = "模板描述长度不能超过512个字符")
	private String remark;
}
