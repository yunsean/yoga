package com.yoga.admin.template.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TemplateListDto extends BaseDto {

	@ApiModelProperty(value = "模板名称过滤")
	private String name;
}
