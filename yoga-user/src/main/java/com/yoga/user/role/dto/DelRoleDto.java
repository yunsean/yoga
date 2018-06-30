package com.yoga.user.role.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class DelRoleDto extends TenantDto {

	@NotNull(message = "未指定角色ID")
	private Long id;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
