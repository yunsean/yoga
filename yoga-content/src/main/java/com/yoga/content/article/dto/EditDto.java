package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class EditDto extends TenantDto {

	private String id;
	@NotNull(message = "未指定栏目")
	private Long columnId;
	private String templateCode;
	@NotNull(message = "未指定标题")
	private String title;
	private boolean alone;

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

	public boolean isAlone() {
		return alone;
	}
	public void setAlone(boolean alone) {
		this.alone = alone;
	}
}
