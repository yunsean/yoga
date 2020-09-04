package com.yoga.admin.branch.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class BranchAddDto extends BaseDto {

	@ApiModelProperty(value = "父级部门ID，默认值为根部门")
	private long parentId = 0;
	@ApiModelProperty(value = "部门名称", required = true)
	@NotEmpty(message = "部门名称不能为空")
	private String name;
	@ApiModelProperty(value = "部门描述")
	private String remark;
	@ApiModelProperty(value = "赋予角色ID列表")
	private Long[] roleIds;
}
