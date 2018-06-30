package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class UpdateDto extends TenantDto {

	@NotNull(message = "未指定文章ID")
	private String id;
	private Long columnId;
	private String templateCode;
	private String title;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

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
