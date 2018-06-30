package com.yoga.user.role.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class SaveSettingDto extends TenantDto {

	@NotNull(message = "未指定默认角色ID")
	private Long roleId;
	private String roleName;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
