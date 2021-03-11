package com.yoga.weixinapp.shiro;

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
public class WechatPrincipal implements Serializable {
    private long tenantId;
    private long userId;
    private AuthorizationInfo authorizationInfo = null;
}
