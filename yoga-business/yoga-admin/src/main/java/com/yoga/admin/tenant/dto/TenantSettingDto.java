package com.yoga.admin.tenant.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TenantSettingDto extends BaseDto {

	@NotNull(message = "请选择租户")
	private Long tenantId;
}
