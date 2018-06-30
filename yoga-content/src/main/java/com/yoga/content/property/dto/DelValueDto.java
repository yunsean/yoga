package com.yoga.content.property.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class DelValueDto extends TenantDto {

	@NotNull(message = "选项值ID不能为空")
	private Long id;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
