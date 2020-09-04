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
public class DutyUpdateDto extends BaseDto {
    @ApiModelProperty(value = "职级ID", required = true)
    @NotNull(message = "dutyId为空")
    private Long id;
    @ApiModelProperty(value = "新的职级名称，不为空则更新")
    private String name;
    @ApiModelProperty(value = "新的职级编码，传值则更新")
    private String code;
    @ApiModelProperty(value = "新的职级描述，传值则更新")
    private String remark;
    @ApiModelProperty(value = "新的职级权限ID列表，传值则更新")
    private Long[] roleIds;
}
