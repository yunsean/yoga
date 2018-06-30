package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

/**
 * Created on 2017/8/22
 **/

public class DetailsByFilterDto extends TenantDto {
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
