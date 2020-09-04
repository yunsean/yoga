package com.yoga.admin.user.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserGetDto extends BaseDto {
    @ApiModelProperty(value = "用户ID，不传值则获取当前登录用户的信息")
    private Long id;
}
