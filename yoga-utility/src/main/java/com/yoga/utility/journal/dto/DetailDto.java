package com.yoga.utility.journal.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class DetailDto extends TenantDto {

    @NotNull(message = "日志ID不能为空")
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
