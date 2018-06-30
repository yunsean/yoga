package com.yoga.imessager.group.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class AcceptDto extends TenantDto {

    @NotNull(message = "群组ID不能为空")
    private Long id;
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
