package com.yoga.content.column.dto;

import com.yoga.user.basic.TenantDto;

public class ListDto extends TenantDto {

	private Long parentId;
	private String parentCode;
	private String code;
	private Boolean enabled;

	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
