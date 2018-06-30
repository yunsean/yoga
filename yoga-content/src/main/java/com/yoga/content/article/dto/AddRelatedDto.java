package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

public class AddRelatedDto extends TenantDto {

	private String fieldCode;
	private String filter;

	public String getFieldCode() {
		return fieldCode;
	}
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
}
