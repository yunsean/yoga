package com.yoga.wechat.oauth2

import com.yoga.core.exception.BusinessException
import com.yoga.core.interfaces.wechat.IdentityType
import com.yoga.core.service.BaseService
import com.yoga.user.model.LoginUser
import com.yoga.wechat.exception.NotBindException
import com.yoga.wechat.exception.NotInWechatException
import com.yoga.wechat.shiro.WechatToken
import com.yoga.wechat.users.User
import com.yoga.wechat.users.UserService
import com.yoga.wechat.weixin.WeixinServiceFactory
import me.chanjar.weixin.common.exception.WxErrorException
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest

@Service("wechatOAuth2Service")
open class OAuth2Service @Autowired constructor(
        val weixinServiceFactory: WeixinServiceFactory,
        val userService: UserService): BaseService() {

    open fun login(request: HttpServletRequest, redirectTo: String? = null): LoginUser {
        val token = WechatToken(weixinServiceFactory, request)
        if (token.openId.isNullOrEmpty() || token.accountId == null) throw NotInWechatException(redirectTo)
        val subject = SecurityUtils.getSubject()
        subject.session.timeout = 15L * 24 * 3600 * 1000
        try {
            subject.login(token)
        } catch (e: AuthenticationException) {
            throw NotBindException(token.openId!!, token.accountId!!, redirectTo)
        }
        return subject.session.getAttribute("login") as LoginUser
    }

    open fun token(request: HttpServletRequest): WechatToken {
        val token = WechatToken(weixinServiceFactory, request)
        if (token.openId.isNullOrEmpty() || token.accountId == null) throw NotInWechatException()
        return token
    }

    open fun getOpenId(request: HttpServletRequest?, accountId: Long? = null): String? {
        if (request == null) return null
        val code = request.getParameter("code")
        if (code.isNullOrEmpty()) return null
        var accountId = accountId
        if (accountId == null) {
            var aid = request.getParameter("state")
            if (aid == null) aid = request.getHeader("state")
            accountId = aid?.toLongOrNull()
        }
        if (accountId == null) return null
        val wxService = weixinServiceFactory.getService(accountId)
        try {
            return wxService.oauth2getAccessToken(code)?.openId
        } catch (e: WxErrorException) {
            throw BusinessException(e.message)
        }
    }

    open fun getUserInfo(request: HttpServletRequest?, tenantId: Long? = null, accountId: Long? = null): User? {
        if (request == null) return null
        val code = request.getParameter("code")
        if (code.isNullOrEmpty()) return null
        var accountId = accountId
        if (accountId == null) {
            var aid = request.getParameter("state")
            if (aid == null) aid = request.getHeader("state")
            accountId = aid?.toLongOrNull()
        }
        if (accountId == null) return null
        var tenantId = tenantId
        if (tenantId == null) {
            var tid = request.getParameter("tid")
            if (tid == null) tid = request.getHeader("tid")
            tenantId = tid?.toLongOrNull()
        }
        val wxService = weixinServiceFactory.getService(accountId)
        var wxMpOAuth2AccessToken = try {
            wxService.oauth2getAccessToken(code)
        } catch (e: WxErrorException) {
            throw BusinessException(e.message)
        }
        val openId = wxMpOAuth2AccessToken.openId
        return runInLock("com.yoga.wechat.oauth2.${accountId}", object: LockRunnable<User?> {
            override fun run(): User? {
                val user = userService.get(tenantId!!, accountId!!, openId)
                if (user != null) return user
                if (!wxService.oauth2validateAccessToken(wxMpOAuth2AccessToken)) {
                    try {
                        wxMpOAuth2AccessToken = wxService.oauth2refreshAccessToken(wxMpOAuth2AccessToken.refreshToken)
                    } catch (e: WxErrorException) {
                        throw BusinessException(e.message)
                    }
                }
                val userInfo = try {
                    wxService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN")
                } catch (e: WxErrorException) {
                    throw BusinessException(e.message)
                }
                return userService.add(tenantId!!, accountId!!, userInfo)
            }
        })
    }

    fun getUserInfoUrl(accountId: Long, redirectUri: String?): String {
        val wxService = weixinServiceFactory.getService(accountId)
        return wxService.oauth2buildAuthorizationUrl(redirectUri, "snsapi_userinfo", accountId.toString())
    }

    fun getOpenIdUrl(accountId: Long, redirectUri: String?): String {
        val wxService = weixinServiceFactory.getService(accountId)
        return wxService.oauth2buildAuthorizationUrl(redirectUri, "snsapi_base", accountId.toString())
    }

    fun getUrlAuthType(redirectUri: String?): Pair<String?, IdentityType> {
        if (redirectUri.isNullOrEmpty()) return Pair(redirectUri, IdentityType.none)
        val pattern = "https://open\\.weixin\\.qq\\.com/connect/oauth2/authorize\\?appid=(.*)&redirect_uri=(.+)&response_type=code&scope=(snsapi_userinfo|snsapi_base)&state=(.*)#wechat_redirect"
        val r = Pattern.compile(pattern)
        val match = r.matcher(redirectUri)
        if (!match.find()) return Pair(redirectUri, IdentityType.none)
        val url = match.group(1)
        val type = match.group(2)
        if (type.equals("snsapi_userinfo")) return Pair(url, IdentityType.userInfo)
        else return Pair(url, IdentityType.openId)
    }
}