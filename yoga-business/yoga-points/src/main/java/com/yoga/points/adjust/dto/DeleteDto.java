package com.yoga.points.adjust.dto;


import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class DeleteDto extends BaseDto {

    @ApiModelProperty(value = "积分ID", required = true)
    @NotNull(message = "积分ID不能为空")
    private Long id;
}
