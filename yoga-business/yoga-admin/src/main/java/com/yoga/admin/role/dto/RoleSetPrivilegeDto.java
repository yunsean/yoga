package com.yoga.admin.role.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class RoleSetPrivilegeDto extends BaseDto {

    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull(message = "未指定角色信息")
    private Long id;
    @ApiModelProperty(value = "需要赋予的权限编码列表，权限编码从menu.xml中查看", required = true)
    @NotNull(message = "未赋予任何权限")
    @NotEmpty(message = "未赋予任何权限")
    private String[] privileges;
}
