package com.yoga.user.duty.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class UpdateDutyDto extends TenantDto {
    @NotNull(message = "dutyId为空")
    private Long id;
    private String name;
    private String remark;
    private long[] roleIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(long[] roleIds) {
        this.roleIds = roleIds;
    }
}
