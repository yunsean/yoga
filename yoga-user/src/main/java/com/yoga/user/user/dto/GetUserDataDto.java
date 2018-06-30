package com.yoga.user.user.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class GetUserDataDto extends TenantDto {

    @NotNull(message = "KEY不能为空")
    private String key;
    private boolean optional = false;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }
}
