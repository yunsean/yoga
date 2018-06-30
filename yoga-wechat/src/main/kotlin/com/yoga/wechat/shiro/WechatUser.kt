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

class WechatUser(id: Long,
                 username: String = "",
                 nickname: String? = null,
                 avatar: String? = null,
                 token: String? = null,
                 var openId: String? = null,
                 var accountId: Long = 0L)
    : LoginUser(id, username, nickname, avatar, token) {
    constructor(): this(0L) {
        try {
            val user = SecurityUtils.getSubject().session.getAttribute("login")
            if (user is LoginUser) {
                this.id = user.getId()
                this.username = user.getUsername()
                this.nickname = user.getNickname()
                this.avatar = user.getAvatar()
                this.token = user.getToken()
            }
            if (user is WechatUser) {
                this.openId = user.openId
                this.accountId = user.accountId
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    constructor(user: LoginUser, openId: String = "", accountId: Long = 0L): this(user.id,
            user.username ?: "",
            user.nickname ?: "",
            user.avatar ?: "",
            user.token ?: "",
            openId, accountId) {
    }
}
