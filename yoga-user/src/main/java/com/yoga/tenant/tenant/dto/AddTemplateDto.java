package com.yoga.tenant.tenant.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddTemplateDto extends TenantDto {

	@NotNull(message = "模板名称不能为空")
	@Size(min = 1, max = 100, message = "模板名称长度只能在1-100个字符之间")
	private String name;
	@Size(max = 512, message = "模板描述长度不能超过512个字符")
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
