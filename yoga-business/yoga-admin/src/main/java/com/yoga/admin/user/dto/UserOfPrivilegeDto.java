package com.yoga.admin.user.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserOfPrivilegeDto extends BaseDto {

    @ApiModelProperty(value = "权限编码", required = true)
    @NotBlank(message = "请指定权限")
    private String privilege;
    @ApiModelProperty(value = "用户名或者姓名部分字段")
    private String filter;
    @ApiModelProperty(value = "查询人所在部门")
    private Long branchId;
    @ApiModelProperty(value = "查询人所属职级")
    private Long dutyId;
    @ApiModelProperty(value = "查询人所属职级编码")
    private String dutyCode;
    @ApiModelProperty(value = "查询大于此级别的用户")
    private Integer levelAbove;
}
