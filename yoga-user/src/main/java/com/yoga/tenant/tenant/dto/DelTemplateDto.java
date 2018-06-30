package com.yoga.tenant.tenant.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class DelTemplateDto extends TenantDto {

	@NotNull(message = "请输入模板ID")
	private Long id = null;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
