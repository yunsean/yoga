package com.yoga.content.column.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UpdateDto extends BaseDto {

	@NotNull(message = "栏目ID不能为空")
	private Long id;
	private String code;
	private String name;
	private Long templateId;
	private String remark;
	private Long parentId;
	private Boolean enabled = true;
	private Boolean hidden = false;
}
