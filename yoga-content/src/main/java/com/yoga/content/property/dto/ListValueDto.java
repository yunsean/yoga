package com.yoga.content.property.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class ListValueDto extends TenantDto {

	@NotNull(message = "未指定选项编码")
	private String code;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
