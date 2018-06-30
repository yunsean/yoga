package com.yoga.tenant.tenant.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class SetTemplateModelDto extends TenantDto {

	@NotNull(message = "请输入模板ID")
	private Long templateId;
	@NotNull(message = "未选定任何模块")
	private String[] modules;

	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String[] getModules() {
		return modules;
	}
	public void setModules(String[] modules) {
		this.modules = modules;
	}
}
