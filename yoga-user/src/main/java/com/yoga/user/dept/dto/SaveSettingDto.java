package com.yoga.user.dept.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class SaveSettingDto extends TenantDto {

	@NotNull(message = "未指定默认部门ID")
	private Long deptId;
	private String deptName;

	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
