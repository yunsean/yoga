package com.yoga.admin.role.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class RoleDeleteDto extends BaseDto {

	@ApiModelProperty(value = "角色ID", required = true)
	@NotNull(message = "未指定角色ID")
	private Long id;
}
