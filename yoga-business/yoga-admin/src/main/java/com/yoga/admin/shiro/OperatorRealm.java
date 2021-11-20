package com.yoga.admin.shiro;

import com.yoga.core.exception.BusinessException;
import com.yoga.logging.service.LoggingService;
import com.yoga.operator.user.model.User;
import com.yoga.operator.user.service.UserService;
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

import java.util.HashSet;
import java.util.Set;

@Component
public class OperatorRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private SuperAdminUser superAdminUser;
    @Autowired
    @Lazy
    private UserService userService;

    public final static String OperatorRole = "operator";
    private final static Set<String> operatorRoles = new HashSet<String>(){{
        add("operator");
    }};

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object principal = super.getAvailablePrincipal(principalCollection);
        if (principal instanceof OperatorPrincipal) return ((OperatorPrincipal)principal).getAuthorizationInfo();
        else return null;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OperatorToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        OperatorToken token = (OperatorToken) authenticationToken;
        User user;
        SimpleAuthorizationInfo info;
        if (superAdminUser.isAdmin(token.getUsername())) {
            if (superAdminUser.passwordMatches(String.valueOf(token.getPassword()))) throw new BusinessException("用户名不存在或者密码错误！");
            user = superAdminUser.getAdminInfo();
            info = new SimpleAuthorizationInfo();
            info.setRoles(operatorRoles);
            info.setStringPermissions(superAdminUser.getPermissions());
        } else {
            user = userService.login(token.getTenantId(), token.getUsername(), String.valueOf(token.getPassword()));
            info = new SimpleAuthorizationInfo();
            info.setRoles(operatorRoles);
            info.setStringPermissions(userService.getPrivileges(user.getTenantId(), user.getId()));
        }
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("user", user);
        session.setAttribute("permissions", info.getStringPermissions());
        LoggingService.add(user.getId(), UserService.ModuleName, "管理员登录", user.getId());
        return new SimpleAuthenticationInfo(new OperatorPrincipal(user.getTenantId(), user.getId(), info), token.getPassword(), this.getName());
    }
}
