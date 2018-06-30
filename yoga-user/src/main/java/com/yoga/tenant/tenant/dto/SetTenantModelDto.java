package com.yoga.tenant.tenant.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class SetTenantModelDto extends TenantDto {

	private long tenantId;
	@NotNull(message = "未选定任何模块")
	private String[] modules;

	public long getTenantId() {
		return tenantId;
	}
	public void setTenantId(long tenantId) {
		this.tenantId = tenantId;
	}

	public String[] getModules() {
		return modules;
	}
	public void setModules(String[] modules) {
		this.modules = modules;
	}
}
