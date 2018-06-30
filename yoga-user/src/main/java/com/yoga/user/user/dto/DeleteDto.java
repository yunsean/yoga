package com.yoga.user.user.dto;

import com.yoga.user.basic.TenantDto;

public class DeleteDto extends TenantDto {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
