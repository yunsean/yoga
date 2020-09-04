package com.yoga.admin.branch.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class BranchUpdateDto extends BaseDto {

	@ApiModelProperty(value = "部门ID", required = true)
	@NotNull(message = "未指定部门ID")
	private Long id;
	@ApiModelProperty(value = "部门名称，不为空则更新")
	private String name;
	@ApiModelProperty(value = "部门描述，传值则更新")
	private String remark;
	@ApiModelProperty(value = "父级部门ID，传值则更新")
	private Long parentId;
	@ApiModelProperty(value = "赋予角色列表，传值则更新")
	private Long[] roleIds;
}
