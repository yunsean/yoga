package com.yoga.admin.role.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class RoleUpdateDto extends BaseDto {
	@ApiModelProperty(value = "角色ID", required = true)
	@NotNull(message = "未指定角色ID")
	private Long id;
	@ApiModelProperty("新角色名称，不为空则更新")
	private String name;
	@ApiModelProperty("新角色描述，传值（含空字符串）则更新")
	private String remark;
}
