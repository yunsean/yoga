package com.yoga.user.user.dto;

import com.yoga.user.basic.TenantDto;


public class UserInfoDto extends TenantDto {
    private Long id;
    private Long pageIndex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Long pageIndex) {
        this.pageIndex = pageIndex;
    }
}
