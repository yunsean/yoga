package com.yoga.content.column.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class AddDto extends TenantDto {

	@NotNull(message = "栏目编码不能为空")
	private String code;
	@NotNull(message = "栏目名称不能为空")
	private String name;
	@NotNull(message = "栏目模板ID不能为空")
	private Long templateId;
	private long parentId = 0;
	private boolean enabled = true;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
