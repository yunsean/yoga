package com.yoga.wechat.exception

import com.yoga.wechat.oauth2.OAuth2EndController
import java.net.URLEncoder

class NotBindException(val openId: String,
                       var accountId: Long,
                       val redirectTo: String? = null,
                       message: String? = null) : RuntimeException(message) {

    fun redirect(): String = OAuth2EndController.Url_NotBind + "?openId=${openId}&accountId=${accountId}" + if (!redirectTo.isNullOrBlank()) ("&uri=" + URLEncoder.encode(redirectTo, "UTF-8")) else ""
    companion object {
        private val serialVersionUID = 28276149133604363L
    }
}