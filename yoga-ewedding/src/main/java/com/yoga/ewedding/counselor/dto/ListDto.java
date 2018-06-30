package com.yoga.ewedding.counselor.dto;

import com.yoga.ewedding.counselor.enums.CounselorStatus;
import com.yoga.user.basic.TenantDto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class ListDto extends TenantDto {

    private Long typeId;
    @Enumerated(EnumType.STRING)
    private CounselorStatus status;
    private String name;
    private String company;

    public Long getTypeId() {
        return typeId;
    }
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public CounselorStatus getStatus() {
        return status;
    }
    public void setStatus(CounselorStatus status) {
        this.status = status;
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
