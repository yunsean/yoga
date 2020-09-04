package com.yoga.content.template.dto;

import com.yoga.content.template.enums.FieldType;
import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UpdateFieldDto extends BaseDto {

	@NotNull(message = "字段ID不能为空")
	private Long id;
	private String code;
	private String name;
	private String hint;
	@Enumerated(EnumType.STRING)
	private FieldType type;
	private String param;
	private String remark;
	private String placeholder;
	private Boolean enabled;
}
