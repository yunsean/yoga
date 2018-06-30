package com.yoga.tenant.sms.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

public class SaveSettingDto extends TenantDto {

	@NotEmpty(message = "请选择短信网关")
	private String service;

	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
}
