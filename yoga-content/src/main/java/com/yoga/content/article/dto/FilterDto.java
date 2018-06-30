package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

public class FilterDto extends TenantDto {

	private Long columnId;
	private String title;

	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
