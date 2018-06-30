package com.yoga.wechat.shiro

import com.yoga.user.model.LoginUser
import com.yoga.user.role.dao.PermissionDAO
import com.yoga.user.shiro.TenantUser
import com.yoga.wechat.account.AccountService
import com.yoga.wechat.users.UserService
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.SimpleAuthenticationInfo
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class WechatRealm @Autowired constructor(
        val accountService: AccountService,
        val userService: UserService,
        val permissionDAO: PermissionDAO) : AuthorizingRealm() {

    override fun doGetAuthorizationInfo(principalCollection: PrincipalCollection): AuthorizationInfo? {
        val principal = super.getAvailablePrincipal(principalCollection)
        return (principal as? TenantUser)?.authorizationInfo
    }

    override fun supports(token: AuthenticationToken?): Boolean {
        return token is WechatToken && token.accountId != null && !token.openId.isNullOrEmpty()
    }

    @Throws(AuthenticationException::class)
    override fun doGetAuthenticationInfo(authenticationToken: AuthenticationToken): AuthenticationInfo? {
        val token = authenticationToken as WechatToken
        val user = userService.getBindUser(token.accountId!!, token.openId!!)
        if (user == null) throw AuthenticationException("微信用户尚未绑定")
        val session = SecurityUtils.getSubject().session
        val sessionId = session.id.toString()
        session.setAttribute("user", user)
        session.setAttribute("login", WechatUser(user.id, user.username, user.fullname, user.avatar, sessionId,
                token.openId!!, token.accountId!!))

        val info = SimpleAuthorizationInfo()
        val permissions = permissionDAO.getUserPermissions(user)
        info.stringPermissions = permissions
        return SimpleAuthenticationInfo(TenantUser(user.tenantId, user.id, info), token.credentials, this.name)
    }
}
