package com.yoga.tenant.tenant.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.Min;

public class DelTenantDto extends TenantDto {

	@Min(value = 1, message = "系统站点无法删除")
	private long id;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
