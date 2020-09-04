package com.yoga.admin.user.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UserEnableDto extends BaseDto {
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "请选择用户")
    private Long id;
    @ApiModelProperty(value = "是否启用，默认值为false")
    private boolean enable;
}
