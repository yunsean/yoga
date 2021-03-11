package com.yoga.weixinapp.shiro;

import com.yoga.logging.service.LoggingService;
import com.yoga.operator.user.model.User;
import com.yoga.operator.user.service.UserService;
import com.yoga.weixinapp.model.WxmpBindUser;
import com.yoga.weixinapp.service.WxmpUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("miniProgramRealm")
public class WechatRealm extends AuthorizingRealm {

    @Autowired
    private WxmpUserService wxmpUserService;
    @Lazy
    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object principal = super.getAvailablePrincipal(principalCollection);
        if (principal instanceof WechatPrincipal) return ((WechatPrincipal)principal).getAuthorizationInfo();
        else return null;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof WechatToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        WechatToken token = (WechatToken) authenticationToken;
        WxmpBindUser bindUser = wxmpUserService.getUser(token.getTenantId(), token.getUsername());
        if (bindUser == null) throw new AuthenticationException("用户未绑定");
        User user = userService.login(token.getTenantId(), bindUser.getUserId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(userService.getPrivileges(user.getTenantId(), user.getId()));

        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("user", user);
        session.setAttribute("permissions", info.getStringPermissions());
        LoggingService.add(user.getId(), UserService.ModuleName, "微信登录", user.getId());
        return new SimpleAuthenticationInfo(new WechatPrincipal(user.getTenantId(), user.getId(), info), token.getPassword(), this.getName());
    }
}
