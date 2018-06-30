package com.yoga.content.column.dto;

import com.yoga.user.basic.TenantDto;

public class FilterDto extends TenantDto {

	private String filter;

	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
}
