package com.yoga.content.template.dto;

import com.yoga.user.basic.TenantDto;

public class ListDto extends TenantDto {

	private String filter;

	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
}
