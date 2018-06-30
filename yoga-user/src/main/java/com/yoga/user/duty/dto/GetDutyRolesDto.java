package com.yoga.user.duty.dto;

import com.yoga.user.basic.TenantDto;

public class GetDutyRolesDto extends TenantDto {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
