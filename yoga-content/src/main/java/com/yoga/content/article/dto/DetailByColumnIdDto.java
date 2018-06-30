package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

/**
 * Created on 2017/8/21
 **/

public class DetailByColumnIdDto extends TenantDto {
    @NotNull(message = "栏目ID不能为空")
    private Long columnId;
    private String[] fields;

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }
}
