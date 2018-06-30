package com.yoga.user.user.dto;

import com.yoga.user.basic.TenantDto;

public class ListDto extends TenantDto {

    private String name = "";
    private Long deptId = null;
    private Long dutyId = null;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getDeptId() {
        return deptId;
    }
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDutyId() {
        return dutyId;
    }
    public void setDutyId(Long dutyId) {
        this.dutyId = dutyId;
    }
}
