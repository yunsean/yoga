package com.yoga.admin.shiro;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.UsernamePasswordToken;

public class OperatorToken extends UsernamePasswordToken {

    @Getter
    @Setter
    private long tenantId;

    public OperatorToken(long tenantId, String username, String password) {
        super(username, password);
        this.tenantId = tenantId;
    }
}
