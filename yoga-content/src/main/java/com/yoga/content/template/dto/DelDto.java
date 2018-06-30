package com.yoga.content.template.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class DelDto extends TenantDto {

	@NotNull(message = "模板ID不能为空")
	private Long id;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
