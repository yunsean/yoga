package com.yoga.utility.district.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DistrictGetDto {
    @ApiModelProperty(value = "区域ID", required = true)
    @NotNull(message = "区域ID不能为空")
    private Long id;
}
