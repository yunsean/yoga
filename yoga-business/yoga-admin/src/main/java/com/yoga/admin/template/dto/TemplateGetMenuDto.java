package com.yoga.admin.template.dto;
import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TemplateGetMenuDto extends BaseDto {

	@ApiModelProperty(value = "模板ID", required = true)
	@NotNull(message = "未指定模板ID")
	private Long templateId;
	@ApiModelProperty(value = "菜单ID", required = true)
	@NotNull(message = "未指定菜单")
	private Long menuId;
}
