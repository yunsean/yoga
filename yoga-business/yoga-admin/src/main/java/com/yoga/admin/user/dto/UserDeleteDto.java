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
public class UserDeleteDto extends BaseDto {

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "请指定要删除的用户")
    private Long id;
}
