package com.yoga.content.template.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class ListFieldDto extends TenantDto {

	@NotNull(message = "模板ID不能为空")
	private Long templateId;

	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
}
