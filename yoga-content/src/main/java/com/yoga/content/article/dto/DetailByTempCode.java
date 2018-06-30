package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

/**
 * Created on 2017/8/21
 **/

public class DetailByTempCode extends TenantDto {
    @NotNull(message = "模板ID不能为空")
    private String templateCode;
    private String[] fields;

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }
}
