package com.yoga.ewedding.messager.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class InfoDto extends TenantDto{

    @NotNull(message = "用户ID不能为空")
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
