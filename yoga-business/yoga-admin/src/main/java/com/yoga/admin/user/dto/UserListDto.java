package com.yoga.admin.user.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserListDto extends BaseDto {

    @ApiModelProperty(value = "关键字过滤，用户名，姓名")
    private String filter = "";
    @ApiModelProperty(value = "所属部门过滤")
    private Long branchId = null;
    @ApiModelProperty(value = "所属职级过滤")
    private Long dutyId = null;
    @ApiModelProperty(value = "查询人所属职级编码")
    private String dutyCode;
    @ApiModelProperty(value = "查询大于此级别的用户")
    private Integer levelAbove;
}
