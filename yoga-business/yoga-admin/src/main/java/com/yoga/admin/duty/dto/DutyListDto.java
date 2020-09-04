package com.yoga.admin.duty.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class DutyListDto extends BaseDto {

    @ApiModelProperty(value = "查询关键字，比如名称")
    private String filter;
}
