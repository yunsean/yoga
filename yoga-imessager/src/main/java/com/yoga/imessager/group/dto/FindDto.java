package com.yoga.imessager.group.dto;

import com.yoga.user.basic.TenantDto;

public class FindDto extends TenantDto {

    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
