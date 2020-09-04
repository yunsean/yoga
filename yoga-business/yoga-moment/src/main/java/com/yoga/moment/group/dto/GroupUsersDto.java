package com.yoga.moment.group.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class GroupUsersDto extends BaseDto {

    @NotNull(message = "群组ID不能为空")
    private Long groupId;
    private Long branchId;
    private Long dutyId;
    private String filter;
    private boolean includedOnly;
}
