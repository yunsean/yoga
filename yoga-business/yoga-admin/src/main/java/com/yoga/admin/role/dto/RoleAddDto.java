package com.yoga.admin.role.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class RoleAddDto extends BaseDto {
	@ApiModelProperty(value = "角色名称", required = true)
	@NotNull(message = "角色名称不能为空")
	@Size(min = 1, max = 20, message = "角色代码长度只能在1-50个字符之间")
	private String name;
	@ApiModelProperty(value = "角色描述")
	private String remark;
}
