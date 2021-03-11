package com.yoga.weixinapp.shiro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.UsernamePasswordToken;

public class WechatToken extends UsernamePasswordToken {

    @Getter
    @Setter
    private long tenantId;

    public WechatToken(long tenantId, String openId) {
        super(openId, openId);
        this.tenantId = tenantId;
    }
}
