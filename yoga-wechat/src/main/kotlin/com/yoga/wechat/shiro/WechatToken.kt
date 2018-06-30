package com.yoga.wechat.shiro

import com.yoga.core.exception.BusinessException
import com.yoga.wechat.weixin.WeixinServiceFactory
import me.chanjar.weixin.common.exception.WxErrorException
import org.apache.shiro.authc.AuthenticationToken
import java.util.logging.Logger
import javax.servlet.http.HttpServletRequest

class WechatToken(
        val factory: WeixinServiceFactory,
        val request: HttpServletRequest,
        var accountId: Long? = null): AuthenticationToken {

    var openId: String? = null
    get

    init {
        val code = request.getParameter("code")
        if (accountId == null) {
            var aid = request.getParameter("state")
            if (aid == null) aid = request.getHeader("state")
            accountId = aid?.toLongOrNull()
        }
        Logger.getLogger("Dylan").info("wechat token: code=${code}, aid=${accountId}")
        if (accountId != null && !code.isNullOrEmpty()) {
            try {
                openId = factory.getService(accountId!!).oauth2getAccessToken(code)?.openId
            } catch (e: WxErrorException) {
                e.printStackTrace()
            }
        }
    }

    override fun getCredentials(): Any? = ""
    override fun getPrincipal(): Any? = openId
}
