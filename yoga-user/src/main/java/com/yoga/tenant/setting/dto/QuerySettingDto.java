package com.yoga.tenant.setting.dto;

import com.yoga.user.basic.TenantDto;

public class QuerySettingDto extends TenantDto {

	private String filter;

	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
}
