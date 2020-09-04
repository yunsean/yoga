package com.yoga.admin.user.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UserHighestDto extends BaseDto {
    @ApiModelProperty(value = "部门ID，查询结果包含子部门")
    @NotNull(message = "部门ID不能为空")
    private Long branchId;
}
