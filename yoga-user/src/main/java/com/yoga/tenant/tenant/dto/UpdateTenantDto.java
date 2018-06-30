package com.yoga.tenant.tenant.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateTenantDto extends TenantDto {

	private long id;
	@NotNull(message = "租户名称不能为空")
	@Size(min = 1, max = 100, message = "租户名称长度只能在1-100个字符之间")
	private String name;
	@Size(max = 512, message = "租户描述长度不能超过512个字符")
	private String remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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
