package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

public class ListDto extends TenantDto {

	private boolean onlineOnly = true;
	private Long columnId;

	private String columnCode;
	private String[] fields;

	private String name;
	private String code;

	private String templateId;
	private String type;

	public boolean isOnlineOnly() {
		return onlineOnly;
	}
	public void setOnlineOnly(boolean onlineOnly) {
		this.onlineOnly = onlineOnly;
	}

	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public String getColumnCode() {
		return columnCode;
	}
	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}

	public String[] getFields() {
		return fields;
	}
	public void setFields(String[] fields) {
		this.fields = fields;
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

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
