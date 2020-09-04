package com.yoga.content.property.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ListValueDto extends BaseDto {

	@NotNull(message = "未指定选项编码")
	private String code;
}
