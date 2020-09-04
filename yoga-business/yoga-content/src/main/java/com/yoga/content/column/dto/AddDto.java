package com.yoga.content.column.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AddDto extends BaseDto {

	@NotNull(message = "栏目编码不能为空")
	private String code;
	@NotNull(message = "栏目名称不能为空")
	private String name;
	@NotNull(message = "栏目模板ID不能为空")
	private Long templateId;
	private String remark;
	private long parentId = 0;
	private boolean enabled = true;
	private boolean hidden = false;
}
