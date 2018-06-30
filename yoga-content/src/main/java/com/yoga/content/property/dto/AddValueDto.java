package com.yoga.content.property.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class AddValueDto extends TenantDto {

	@NotNull(message = "选项ID不能为空")
	private Long parentId;
	@NotNull(message = "选项值不能为空")
	private String name;
	private String code;

	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
