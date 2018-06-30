package com.yoga.tenant.tenant.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class TemplateModelDto extends TenantDto {

	@NotNull(message = "未选定模板")
	private Long templateId;

	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
}
