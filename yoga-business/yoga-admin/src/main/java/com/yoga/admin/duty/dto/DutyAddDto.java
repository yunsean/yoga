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
public class DutyAddDto extends BaseDto {

    @ApiModelProperty(value = "插入点的职务的ID，插入的职务将比他高", required = true)
    private long belowId = 0L;
    @NotNull(message = "职务名称不能为空")
    @Size(min = 1, max = 20, message = "职务名称长度需要在1~20个字符之间")
    @ApiModelProperty(value = "职级名称，20字符以内", required = true)
    private String name;
    @ApiModelProperty(value = "职级描述")
    private String remark;
    @ApiModelProperty(value = "职级编码")
    private String code;
    @ApiModelProperty(value = "职级赋予的角色列表")
    private Long[] roleIds;
}
