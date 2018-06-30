package com.yoga.ewedding.recharge.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class GetDto extends TenantDto{

    private Long typeId;

    public Long getTypeId() {
        return typeId;
    }
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}
