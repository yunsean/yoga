package com.yoga.ewedding.counselor.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class InfoDto extends TenantDto {

    @NotNull(message = "请输入顾问ID")
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
