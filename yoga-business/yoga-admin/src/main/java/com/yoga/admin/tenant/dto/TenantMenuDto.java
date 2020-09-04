package com.yoga.admin.tenant.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TenantMenuDto extends BaseDto {

	@NotNull(message = "未选定租户")
	private Long tenantId;
}
