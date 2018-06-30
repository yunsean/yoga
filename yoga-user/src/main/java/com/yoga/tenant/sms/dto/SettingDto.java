package com.yoga.tenant.sms.dto;

import com.yoga.user.basic.TenantDto;

public class SettingDto extends TenantDto {

	private String service;

	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
}
