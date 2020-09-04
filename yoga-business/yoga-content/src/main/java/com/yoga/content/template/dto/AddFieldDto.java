package com.yoga.content.template.dto;

import com.yoga.content.template.enums.FieldType;
import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AddFieldDto extends BaseDto {

	@NotNull(message = "模板ID不能为空")
	private Long templateId;
	@NotBlank(message = "字段编码不能为空")
	private String code;
	@NotBlank(message = "字段名称不能为空")
	private String name;
	private String hint;
	@Enumerated(EnumType.STRING)
	@NotNull(message = "字段内容不能为空")
	private FieldType type;
	private String param;
	private String remark;
	private String placeholder;
	private boolean enabled = true;
}
