package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class AddDto extends TenantDto {

	@NotNull(message = "未指定栏目")
	private Long columnId;
	private String templateCode;
	@NotNull(message = "未指定标题")
	private String title;

	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
