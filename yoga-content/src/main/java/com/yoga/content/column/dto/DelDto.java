package com.yoga.content.column.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class DelDto extends TenantDto {

	@NotNull(message = "栏目ID不能为空")
	private Long id;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
