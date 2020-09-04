package com.yoga.utility.district.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class DistrictChildDto {

    @ApiModelProperty(value = "父级区域ID，默认为0，根区域")
    private long parentId = 0;
}
