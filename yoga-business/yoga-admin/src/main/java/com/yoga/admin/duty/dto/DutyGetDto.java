package com.yoga.admin.duty.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class DutyGetDto extends BaseDto {
    @ApiModelProperty(value = "职级ID", required = true)
    @NotNull(message = "职务ID不能为空")
    private long id;
}
