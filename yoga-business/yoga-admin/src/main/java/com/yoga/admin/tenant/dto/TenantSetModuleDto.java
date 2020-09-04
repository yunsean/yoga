package com.yoga.admin.tenant.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TenantSetModuleDto extends BaseDto {

	@ApiModelProperty(value = "租户ID", required = true)
	@NotNull(message = "请指定租户")
	private Long tenantId;
	@ApiModelProperty(value = "为租户分配的模块", required = true)
	@NotNull(message = "未选定任何模块")
	private String[] modules;
}
