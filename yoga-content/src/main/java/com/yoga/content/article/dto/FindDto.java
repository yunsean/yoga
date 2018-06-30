package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

public class FindDto extends TenantDto {

	private boolean onlineOnly = true;
	private String[] fields;

	public boolean isOnlineOnly() {
		return onlineOnly;
	}
	public void setOnlineOnly(boolean onlineOnly) {
		this.onlineOnly = onlineOnly;
	}

	public String[] getFields() {
		return fields;
	}
	public void setFields(String[] fields) {
		this.fields = fields;
	}
}
