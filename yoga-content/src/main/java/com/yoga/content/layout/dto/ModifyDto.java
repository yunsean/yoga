package com.yoga.content.layout.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class ModifyDto extends TenantDto {

    @NotNull(message = "模板ID不能为空")
    private Long templateId;
    private Long id;

    public Long getTemplateId() {
        return templateId;
    }
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
