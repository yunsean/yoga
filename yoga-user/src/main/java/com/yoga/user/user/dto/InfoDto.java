package com.yoga.user.user.dto;

import com.yoga.user.basic.TenantDto;

public class InfoDto extends TenantDto {

    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
