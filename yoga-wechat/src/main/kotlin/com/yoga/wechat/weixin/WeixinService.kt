package com.yoga.wechat.weixin

import com.yoga.core.redis.RedisOperator
import com.yoga.core.utils.StrUtil
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.mp.api.WxMpMessageRouter
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class WeixinService(val redisOperator: RedisOperator) : WxMpServiceImpl(), InitializingBean {

    @Value("\${wechat.enable-crypt:false}")
    var isEnableCrypt: Boolean = false
    val wxMpMessageRouter: WxMpMessageRouter = WxMpMessageRouter(this)

    open fun receive(request: HttpServletRequest, response: HttpServletResponse) {
        val signature = request.getParameter("signature")
        val nonce = request.getParameter("nonce")
        val timestamp = request.getParameter("timestamp")
        response.setContentType("text/html;charset=utf-8")
        response.setStatus(HttpServletResponse.SC_OK)
        if (this.isEnableCrypt) {
            if (!this.checkSignature(timestamp, nonce, signature)) {
                logger.info("response to weixin: " + "非法请求")
                response.getWriter().println("非法请求")
                return
            }
            val echostr = request.getParameter("echostr")
            if (StrUtil.isNotBlank(echostr)) {
                logger.info("echostr to weixin: " + "echostr")
                response.getWriter().println(echostr)
                return
            }
        }

        val encryptType = if (StrUtil.isBlank(request.getParameter("encrypt_type"))) "raw"
        else request.getParameter("encrypt_type")
        var inMessage: WxMpXmlMessage? = null
        logger.info("encrypt_type is:" + encryptType)
        if ("raw" == encryptType) {
            inMessage = WxMpXmlMessage.fromXml(request.getInputStream())
        } else if ("aes" == encryptType) {
            val msgSignature = request.getParameter("msg_signature")
            inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature)
        } else {
            logger.info("response to weixin: " + "不可识别的加密类型")
            response.getWriter().println("不可识别的加密类型")
            return
        }

        val outMessage = wxMpMessageRouter!!.route(inMessage)
        if (outMessage != null) {
            if ("raw" == encryptType) {
                logger.info("response to weixin: " + outMessage.toXml())
                response.getWriter().write(outMessage.toXml())
            } else if ("aes" == encryptType) {
                logger.info("response to weixin: " + outMessage.toEncryptedXml(wxMpConfigStorage))
                response.getWriter().write(outMessage.toEncryptedXml(wxMpConfigStorage))
            }
        } else {
            logger.info("response to weixin: \"\"")
            response.getWriter().write("")
        }
    }

//    open fun sendCustomMessage2(message: InMessage, tid: Long, aid: Long): WxMpXmlOutMessage? {
//        if (StrUtil.isEmpty(message.openid)) logger.error("customMessage's openid should not be null")
//        var type = message.type
//        if (StrUtil.isEmpty(type)) type = WxConsts.XML_MSG_TEXT
//        var outMessage: WxMpXmlOutMessage? = null
//        try {
//            if (type == WxConsts.XML_MSG_IMAGE) {
//                outMessage = WxMpXmlOutMessage
//                        .IMAGE()
//                        .toUser(message.openid)
//                        .fromUser(message.fromUser)
//                        .mediaId(message.mediaId)
//                        .build()
//            } else if (type == WxConsts.XML_MSG_VOICE) {
//                outMessage = WxMpXmlOutMessage
//                        .VOICE()
//                        .toUser(message.openid)
//                        .fromUser(message.fromUser)
//                        .mediaId(message.mediaId)
//                        .build()
//            } else if (type == WxConsts.XML_MSG_VIDEO) {
//                outMessage = WxMpXmlOutMessage
//                        .VIDEO()
//                        .toUser(message.openid)
//                        .fromUser(message.fromUser)
//                        .mediaId(message.mediaId)
//                        .build()
//            } else if (type == WxConsts.XML_MSG_NEWS) {
//                val exists = materialArticleRepository?.findByTenantIdAndAccountIdAndMaterialId(112L, 111L, 293L)
//                if (exists == null) logger.error("没有找到 mediaId = ${message.mediaId} 的 article")
//                val outMessage = WxMpXmlOutMessage
//                        .NEWS()
//                        .toUser(message.openid)
//                        .fromUser(message.fromUser)
//                exists?.forEach {
//                    val article = WxMpXmlOutNewsMessage.Item()
//                    article.title = it.title
//                    article.description = it.digest
//                    article.picUrl = it.thumbUrl
//                    article.url = it.clickUrl
//                    outMessage.addArticle(article)
//                }
//                outMessage.build()
//            } else {
//                outMessage = WxMpXmlOutMessage
//                        .TEXT()
//                        .toUser(message.openid)
//                        .fromUser(message.fromUser)
//                        .content(message.content)
//                        .build()
//            }
//            //this.customMessageSend(message)
//            return outMessage
//        } catch (e: WxErrorException) {
//            logger.error(e.message, e)
//            return null
//        }
//    }

    open fun pushTemplateMessage(templateMessage: WxMpTemplateMessage) {
        try {
            this.pushTemplateMessage(templateMessage)
        } catch (e: WxErrorException) {
            logger.error(e.message, e)
        }

    }

    override fun getAccessToken(): String? {
        return redisOperator.get(accessTokenKeyPrefix + wxMpConfigStorage.appId)
    }

    override fun afterPropertiesSet() {
        if (acquireAccessTokenLock()) {
            val accessToken = getAccessToken(true)
            redisOperator.set(accessTokenKeyPrefix + wxMpConfigStorage.appId, accessToken)
            releaseAccessTokenLock()
        }
    }

    private val accessTokenLockExpire = 3 * 60 * 1000           //millis
    private val accessTokenLockKeyPrefix = "sys.accesstoken.lock."
    private val accessTokenKeyPrefix = "sys.accesstoken."
    private fun acquireAccessTokenLock(): Boolean {
        val key = accessTokenLockKeyPrefix + wxMpConfigStorage.appId
        val expires = System.currentTimeMillis() + accessTokenLockExpire + 1
        val expiresStr = expires.toString()
        if (redisOperator.setNX(key, expiresStr)) return true
        val currentValueStr = redisOperator.get(key)
        if (currentValueStr != null && java.lang.Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
            val oldValueStr = redisOperator.getSet(key, expiresStr)
            if (oldValueStr != null && oldValueStr == currentValueStr) return true
        }
        return false
    }

    private fun releaseAccessTokenLock() {
        try {
            redisOperator.remove(accessTokenLockKeyPrefix + wxMpConfigStorage.appId)
        } catch (e: InterruptedException) {
            logger.error(e.message, e)
        }
    }

    companion object {
        var logger = LoggerFactory.getLogger(WeixinService::class.java)
    }

}