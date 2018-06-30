package com.yoga.user.user.dto;


import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class SetUserDataDto extends TenantDto {

    @NotNull(message = "KEY不能为空")
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
