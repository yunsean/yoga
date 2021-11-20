package com.yoga.logging.model;

import org.apache.shiro.SecurityUtils;

public interface LoginUser {
    Long getTenantId();
    Long getId();
    String getName();

    public static LoginUser getLoginUser() {
        return (LoginUser) SecurityUtils.getSubject().getSession().getAttribute("user");
    }
}
