package com.yoga.admin.template.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TemplateMenuDto extends BaseDto {

	@NotNull(message = "未选定租户模板")
	private Long templateId;
}
