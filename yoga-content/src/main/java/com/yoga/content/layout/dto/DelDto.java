package com.yoga.content.layout.dto;

import com.yoga.core.annotation.Explain;
import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class DelDto extends TenantDto {

    @NotNull(message = "布局ID不能为空")
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
