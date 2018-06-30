package com.yoga.user.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class TenantToken extends UsernamePasswordToken {
    private long tenantId;

    public TenantToken(long tenantId, String username, String password) {
        super(username, password);
        this.tenantId = tenantId;
    }

    public long getTenantId() {
        return tenantId;
    }
    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }
}
