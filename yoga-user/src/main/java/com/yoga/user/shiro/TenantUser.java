package com.yoga.user.shiro;

import org.apache.shiro.authz.AuthorizationInfo;

import java.io.Serializable;

public class TenantUser implements Serializable {
    private long tenantId;
    private long userId;
    private AuthorizationInfo authorizationInfo = null;

    public TenantUser(long tenantId, long userId, AuthorizationInfo authorizationInfo) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.authorizationInfo = authorizationInfo;
    }

    public long getTenantId() {
        return tenantId;
    }
    public long getUserId() {
        return userId;
    }
    public AuthorizationInfo getAuthorizationInfo() {
        return authorizationInfo;
    }
}
