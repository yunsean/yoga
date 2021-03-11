package com.yoga.points.summary.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ListDto extends BaseDto {

    @ApiModelProperty(value = "积分年度")
    private Integer year;
    @ApiModelProperty(value = "部门ID")
    private Long branchId;
    @ApiModelProperty(value = "职级ID")
    private Long dutyId;
    @ApiModelProperty(value = "关键字")
    private String keyword;
    @ApiModelProperty(value = "只显示扣分")
    private boolean penaltyOnly = false;
}
