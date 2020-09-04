package com.yoga.content.template.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class SortFieldDto extends BaseDto {

	@NotNull(message = "字段ID不能为空")
	private Long id;
	@NotNull(message = "排序值不能为空")
	private Integer sort;
}
