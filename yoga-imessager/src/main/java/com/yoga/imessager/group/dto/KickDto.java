package com.yoga.imessager.group.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class KickDto extends TenantDto {

    @NotNull(message = "群组ID不能为空")
    private Long id;
    @NotEmpty(message = "用户ID不能为空")
    private long[] userIds;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public long[] getUserIds() {
        return userIds;
    }
    public void setUserIds(long[] userIds) {
        this.userIds = userIds;
    }
}
