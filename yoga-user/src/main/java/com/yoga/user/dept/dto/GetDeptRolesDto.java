package com.yoga.user.dept.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;


public class GetDeptRolesDto extends TenantDto {

    @NotNull(message = "未指定部门ID")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
