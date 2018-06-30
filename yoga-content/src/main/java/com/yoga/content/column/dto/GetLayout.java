package com.yoga.content.column.dto;

import com.yoga.core.annotation.Explain;
import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

/**
 * Created on 2017/8/25
 **/

public class GetLayout extends TenantDto {
    @Explain("样式模板ID")
    @NotNull
    private Long layoutId;

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }
}
