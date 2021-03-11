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
public class StatisticDto extends BaseDto {

    @NotNull(message = "统计年份不能为空")
    @ApiModelProperty(value = "积分年度", required = true)
    private Integer year;
}
