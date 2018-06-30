package com.yoga.user.role.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddRoleDto extends TenantDto {

	@NotNull(message = "角色名称不能为空")
	@Size(min = 1, max = 20, message = "角色代码长度只能在1-50个字符之间")
	private String name;
	
	private String remark;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
