package com.yoga.admin.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class OperatorToken extends UsernamePasswordToken {
    private long tenantId;

    public OperatorToken(long tenantId, String username, String password) {
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
