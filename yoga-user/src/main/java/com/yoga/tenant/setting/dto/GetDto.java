package com.yoga.tenant.setting.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class GetDto extends TenantDto {

	@NotNull(message = "未指定设置参数名称")
	private String key;
	@NotNull(message = "未指定设置模块")
	private String module;

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
}
