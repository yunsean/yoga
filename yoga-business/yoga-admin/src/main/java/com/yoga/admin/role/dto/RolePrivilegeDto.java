package com.yoga.admin.role.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class RolePrivilegeDto extends BaseDto {

    @NotNull(message = "未指定角色信息")
    private Long roleId;
}
