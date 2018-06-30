package com.yoga.user.role.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class SetPermissionDto extends TenantDto {

    @NotNull(message = "未指定角色信息")
    private Long roleId;
    @NotNull(message = "未赋予任何权限")
    @NotEmpty(message = "未赋予任何权限")
    private String[] privileges;

    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String[] getPrivileges() {
        return privileges;
    }
    public void setPrivileges(String[] privileges) {
        this.privileges = privileges;
    }
}
