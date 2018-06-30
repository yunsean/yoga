package com.yoga.wechat.exception

import com.yoga.wechat.oauth2.OAuth2EndController
import java.net.URLEncoder

class NotInWechatException(val redirectTo: String? = null,
                           message: String? = null) : RuntimeException(message) {

    fun redirect(): String = OAuth2EndController.Url_NotInWechat + if (!redirectTo.isNullOrBlank()) ("?uri=" + URLEncoder.encode(redirectTo, "UTF-8")) else ""
    companion object {
        private val serialVersionUID = 28276149133604363L
    }
}
