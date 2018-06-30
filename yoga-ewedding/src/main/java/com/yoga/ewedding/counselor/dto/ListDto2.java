package com.yoga.ewedding.counselor.dto;

import com.yoga.user.basic.TenantDto;

public class ListDto2 extends TenantDto {

    private Long typeId;
    private String name;
    private String company;

    public Long getTypeId() {
        return typeId;
    }
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
}
