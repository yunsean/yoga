package com.yoga.content.property.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class AddDto extends TenantDto {

	@NotNull(message = "选项编码不能为空")
	private String code;
	@NotNull(message = "选项名称不能为空")
	private String name;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
