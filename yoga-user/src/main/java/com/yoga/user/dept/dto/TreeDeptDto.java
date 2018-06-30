package com.yoga.user.dept.dto;

import com.yoga.user.basic.TenantDto;

public class TreeDeptDto extends TenantDto {

	private long parentId = 0;

	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
}
