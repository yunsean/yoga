package com.yoga.content.layout.dto;

import com.yoga.content.enums.LayoutType;
import com.yoga.user.basic.TenantDto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class ListDto extends TenantDto {
    @NotNull(message = "模板ID不能为空")
    private Long templateId;
    private LayoutType type;

    public Long getTemplateId() {
        return templateId;
    }
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public LayoutType getType() {
        return type;
    }
    public void setType(LayoutType type) {
        this.type = type;
    }
}
