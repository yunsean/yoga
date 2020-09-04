package com.yoga.admin.branch.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class BranchGetDto extends BaseDto {

    @ApiModelProperty(value = "部门ID", required = true)
    @NotNull(message = "未指定部门ID")
    private Long id;
}
