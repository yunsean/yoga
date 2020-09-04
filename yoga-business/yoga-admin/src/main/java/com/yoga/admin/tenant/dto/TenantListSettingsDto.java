package com.yoga.admin.tenant.dto;

import com.yoga.core.base.BaseDto;
import com.yoga.setting.annotation.Settable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TenantListSettingsDto extends BaseDto {
	@NotNull(message = "请指定租户")
	private Long tenantId;
	private String name;
}
