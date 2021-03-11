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
public class YearGetDto extends BaseDto {

    @ApiModelProperty(value = "年度ID", required = true)
    @NotNull(message = "年度ID不能为空")
    private Long id;
}
