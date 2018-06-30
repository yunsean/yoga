package com.yoga.user.web.dto;

import com.yoga.user.basic.TenantDto;

public class IndexDto extends TenantDto {

	private String uri;

	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
