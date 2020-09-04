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
public class GroupUpdateDto extends BaseDto {

    @ApiModelProperty(value = "群组ID", required = true)
    @NotNull(message = "群组ID不能为空")
    private Long id;
    @ApiModelProperty(value = "新的群组名称，不为空则更新")
    private String name;
    @ApiModelProperty(value = "新的群组描述，传值则更新")
    private String remark;
}
