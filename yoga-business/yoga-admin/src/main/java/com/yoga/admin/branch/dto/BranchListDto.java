package com.yoga.admin.branch.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BranchListDto extends BaseDto {

	@ApiModelProperty(value = "关键字过滤，部门名称")
	private String name;
}
