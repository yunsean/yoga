package com.yoga.ewedding.counselor.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class SyncDto extends TenantDto {

    private Long version;

    public Long getVersion() {
        return version;
    }
    public void setVersion(Long version) {
        this.version = version;
    }
}
