package com.yoga.imessager.user.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class QuitDto extends TenantDto {

    @NotNull(message = "群组ID不能为空")
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
