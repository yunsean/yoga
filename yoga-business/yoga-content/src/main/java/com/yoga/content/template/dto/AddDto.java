package com.yoga.content.template.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AddDto extends BaseDto {

	@NotNull(message = "模板编码不能为空")
	private String code;
	@NotNull(message = "模板名称不能为空")
	private String name;
	private String remark;
	private boolean enabled = true;
}
