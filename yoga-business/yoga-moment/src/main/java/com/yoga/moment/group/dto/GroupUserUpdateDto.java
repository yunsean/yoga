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
public class GroupUserUpdateDto {

    @ApiModelProperty(value = "群组ID", required = true)
    @NotNull(message = "群组ID不能为空")
    private Long groupId;
    @ApiModelProperty(value = "添加的用户ID")
    private Long[] addIds;
    @ApiModelProperty(value = "删除的用户ID")
    private Long[] deleteIds;
}
