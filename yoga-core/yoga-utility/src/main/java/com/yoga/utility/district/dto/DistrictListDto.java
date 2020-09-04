package com.yoga.utility.district.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DistrictListDto {
    @ApiModelProperty(value = "区域编码", required = true)
    @NotEmpty(message = "编码不能为空")
    private String code;
    @ApiModelProperty(value = "获取区域对应的级别，不传则代表获取最接近的")
    private Integer level;
}
