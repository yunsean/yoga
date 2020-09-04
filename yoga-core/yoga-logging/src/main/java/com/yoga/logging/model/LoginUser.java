package com.yoga.logging.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;

public interface LoginUser {
    Long getTenantId();
    Long getId();
    String getName();

    public static LoginUser getLoginUser() {
        return (LoginUser) SecurityUtils.getSubject().getSession().getAttribute("user");
    }
}
