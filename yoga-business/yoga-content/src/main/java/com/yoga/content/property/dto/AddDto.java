package com.yoga.content.property.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AddDto extends BaseDto {

	private Long parentId = 0L;
	@NotNull(message = "选项编码不能为空")
	private String code;
	@NotNull(message = "选项名称不能为空")
	private String name;
	private String poster;
}
