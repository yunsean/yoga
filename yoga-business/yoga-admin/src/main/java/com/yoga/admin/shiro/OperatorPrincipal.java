package com.yoga.admin.shiro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.authz.AuthorizationInfo;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperatorPrincipal implements Serializable {
    private long tenantId;
    private long userId;
    private AuthorizationInfo authorizationInfo = null;
}
