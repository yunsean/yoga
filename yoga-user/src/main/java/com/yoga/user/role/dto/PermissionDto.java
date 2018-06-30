package com.yoga.user.role.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class PermissionDto extends TenantDto {

    @NotNull(message = "未指定角色信息")
    private Long roleId;

    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
