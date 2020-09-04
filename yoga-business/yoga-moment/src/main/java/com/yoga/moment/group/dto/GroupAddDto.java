package com.yoga.moment.group.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class GroupAddDto extends BaseDto {

    @ApiModelProperty(value = "群组名称", required = true)
    @NotBlank(message = "群组名称不能为空")
    private String name;
    @ApiModelProperty(value = "群组描述")
    private String remark;
}
