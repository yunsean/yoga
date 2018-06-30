package com.yoga.imessager.rongcloud.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class SettingDto extends TenantDto {

	@NotNull(message = "未选定租户")
	private Long tenantId;

	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
