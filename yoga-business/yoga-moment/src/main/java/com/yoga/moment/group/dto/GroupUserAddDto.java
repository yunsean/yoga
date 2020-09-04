package com.yoga.moment.group.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class GroupUserAddDto extends BaseDto {

    @ApiModelProperty(value = "群组ID", required = true)
    @NotNull(message = "群组ID不能为空")
    private Long groupId;
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;
}
