package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

public class FrameDto extends TenantDto {

	private long columnId = 0L;
	private String columnCode;
	private long parentId = 0L;
	private String parentCode;

	public long getColumnId() {
		return columnId;
	}
	public void setColumnId(long columnId) {
		this.columnId = columnId;
	}

	public String getColumnCode() {
		return columnCode;
	}
	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}

	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
}
