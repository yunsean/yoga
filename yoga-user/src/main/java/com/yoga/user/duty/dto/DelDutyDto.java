package com.yoga.user.duty.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class DelDutyDto extends TenantDto {
    @NotNull(message = "职务ID不能为空")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
