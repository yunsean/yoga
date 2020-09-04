package com.yoga.admin.tenant.dto;

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
public class TenantUpdateDto extends BaseDto {

	@ApiModelProperty(value = "租户ID", required = true)
	@NotNull(message = "租户ID不能为空")
	private Long id;
	@ApiModelProperty(value = "新的租户名称，不为空则更新")
	@NotNull(message = "租户名称不能为空")
	@Size(min = 1, max = 100, message = "租户名称长度只能在1-100个字符之间")
	private String name;
	@ApiModelProperty(value = "新的租户描述信息，传值（包含空字符串）则更新")
	@Size(max = 512, message = "租户描述长度不能超过512个字符")
	private String remark;
}
