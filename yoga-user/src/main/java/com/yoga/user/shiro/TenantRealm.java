package com.yoga.user.shiro;

import com.yoga.user.role.dao.PermissionDAO;
import com.yoga.user.model.LoginUser;
import com.yoga.user.user.model.User;
import com.yoga.user.user.repo.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class TenantRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(TenantRealm.class);

    @Autowired
    private SuperAdminUser superAdminUser;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionDAO permissionDAO;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object principal = super.getAvailablePrincipal(principalCollection);
        if (principal instanceof TenantUser) {
            return ((TenantUser)principal).getAuthorizationInfo();
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof TenantToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        TenantToken token = (TenantToken) authenticationToken;
        User user;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());
        if (null == username) return null;
        if (superAdminUser.isAdmin(username)) {
            user = superAdminUser.passwordMatches(password) ? superAdminUser.getAdminInfo() : null;
        } else {
            user = userRepository.findFirstByTenantIdAndPhone(token.getTenantId(), token.getUsername());
        }
        if (user == null && !superAdminUser.isAdmin(username)) {
            user = userRepository.findFirstByTenantIdAndUsername(token.getTenantId(), token.getUsername());
        }
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        } else {
            String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
            String pwd = DigestUtils.md5Hex(String.valueOf(token.getPassword()));
            String savedPwd = user.getPassword();
            if (null == savedPwd || !savedPwd.equals(pwd)) {
                throw new AuthenticationException("无效的用户密码");
            } if (user.getExpire() != null && user.getExpire().before(new Date())) {
                throw new AuthenticationException("用户已过期");
            }
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("user", user);
            session.setAttribute("login", new LoginUser(user.getId(), user.getUsername(), user.getFullname(), user.getAvatar(), sessionId));
            SimpleAuthorizationInfo info;
            if (superAdminUser.isAdmin(username)) {
                info = superAdminUser.assembleAdminInfo();
            } else {
                info = new SimpleAuthorizationInfo();
                Set<String> permissions = permissionDAO.getUserPermissions(user);
                info.setStringPermissions(permissions);
            }
            return new SimpleAuthenticationInfo(new TenantUser(user.getTenantId(), user.getId(), info), token.getPassword(), this.getName());
        }
    }
}
