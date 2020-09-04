package com.yoga.admin.duty.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class DutyGetRolesDto extends BaseDto {
    @NotNull(message = "职称ID不能为空")
    private Long id;
}
