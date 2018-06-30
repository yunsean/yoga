package com.yoga.imessager.group.dto;

import com.yoga.core.annotation.Explain;
import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class DismissDto extends TenantDto {

    @Explain("群组ID")
    @NotNull(message = "群组ID不能为空")
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
