package com.yoga.admin.template.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TemplateGetDto extends BaseDto {

	@ApiModelProperty(value = "模板ID", required = true)
	@NotNull(message = "请输入模板ID")
	private Long id = null;
}
