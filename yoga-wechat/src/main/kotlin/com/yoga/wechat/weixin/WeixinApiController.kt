package com.yoga.wechat.weixin

import com.yoga.core.annotation.Explain
import com.yoga.core.interfaces.wechat.EventType
import com.yoga.core.interfaces.wechat.InMessage
import com.yoga.core.interfaces.wechat.OutMessageType
import com.yoga.wechat.actions.ActionsService
import com.yoga.wechat.base.WxBaseApiController
import com.yoga.wechat.reply.MessageType
import com.yoga.wechat.reply.ReplyService
import me.chanjar.weixin.common.api.WxConsts
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage
import me.chanjar.weixin.mp.util.crypto.WxMpCryptUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Explain(exclude = true)
@RestController
@RequestMapping("/api/wechat")
open class WeixinApiController @Autowired constructor(
        val weixinServiceFactory: WeixinServiceFactory,
        val replyService: ReplyService,
        val actionService: ActionService,
        val actionsService: ActionsService,
        val messageCache: MessageCache) : WxBaseApiController() {

    @RequestMapping("/{tid}/{aid}")
    open fun receivePush(request: HttpServletRequest, response: HttpServletResponse, @PathVariable(value = "aid") accountId: Long, @PathVariable(value = "tid") tenantId: Long) {
        response.contentType = "text/html;charset=utf-8"
        val weixinService = try {
            weixinServiceFactory.getService(accountId ?: 0L)
        } catch (ex: Exception) {
            ex.printStackTrace(); null
        }
        if (weixinService == null) {
            response.status = HttpServletResponse.SC_BAD_REQUEST
            logger.warn("非法请求")
            return;
        }

        response.status = HttpServletResponse.SC_OK
        val signature = request.getParameter("signature")
        val nonce = request.getParameter("nonce")
        val timestamp = request.getParameter("timestamp")
        if (!weixinService.checkSignature(timestamp, nonce, signature)) {
            return logger.warn("非法请求")
        }
        val echostr = request.getParameter("echostr")
        if (!echostr.isNullOrBlank()) {
            response.writer.write(echostr)
            return logger.info("微信服务器验签请求")
        }

        val encryptType = request.getParameter("encrypt_type")
        var inMessage = if (encryptType.isNullOrBlank()) {
            WxMpXmlMessage.fromXml(request.inputStream)
        } else if ("aes".equals(encryptType)) {
            WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), weixinService.wxMpConfigStorage, timestamp, nonce, request.getParameter("msg_signature"));
        } else {
            null
        }
        if (inMessage == null) return response.writer.println("不可识别的加密类型")
        logger.warn("${inMessage.fromUser}-${inMessage.createTime}：${inMessage.event}")

        var message: Message? = null
        if (!messageCache.savePicEventMessage(inMessage)) {
            messageCache.updatePicImageMessage(inMessage)
            message = action(accountId, inMessage) ?: reply(tenantId, accountId, inMessage)
        }
        message?.let {
            it.openid = inMessage.fromUser
            it.fromUser = inMessage.toUser
            if (it.type == "plugin" && !it.pluginCode.isNullOrEmpty()) {
                messageCache.getUserLocation(it.openid)?.let {
                    inMessage.longitude = it.longitude
                    inMessage.latitude = it.latitude
                }
                val result = actionsService.action(it.pluginCode!!)?.action?.onMessage(tenantId, accountId, convertMessage(inMessage), it.content)
                if (result != null) {
                    message = Message(
                            result.type.toString(),
                            if (result.type == OutMessageType.text) result.text else if (result.type == OutMessageType.music) result.thumbMediaId else result.mediaId,
                            null,
                            inMessage.fromUser,
                            inMessage.toUser,
                            it.title,
                            it.description,
                            it.musicUrl,
                            it.hqMusicUrl)
                }
            }
        } ?: messageCache.saveUserLocation(inMessage)
        var resultXml: String? = message?.let { actionService.sendCustomMessage(it, tenantId, accountId)?.toXml() }
        if (resultXml.isNullOrBlank()) resultXml = "success"
        else if (!encryptType.isNullOrBlank()) resultXml = WxMpCryptUtil(weixinService.wxMpConfigStorage).encrypt(resultXml)
        response.getWriter().write(resultXml)
    }

    private fun convertMessage(inMessage: WxMpXmlMessage): InMessage? {
        val message = InMessage()
        with(inMessage) {
            message.toUser = toUser
            message.fromUser = fromUser
            message.createTime = createTime
            message.eventType = when(inMessage.msgType) {
                "event"-> when(inMessage.event) {
                    "subscribe"-> EventType.subscribe
                    "unsubscribe"-> EventType.unsubscribe
                    "CLICK"-> EventType.click
                    else-> return null
                }
                "text"-> EventType.text
                "image"-> EventType.image
                "voice"-> EventType.voice
                "video"-> EventType.video
                "shortvideo" -> EventType.shortvideo
                "location" -> EventType.location
                else-> null
            }
            message.eventKey = eventKey
            message.content = content
            message.menuId = menuId
            message.msgId = msgId
            message.picUrl = picUrl
            message.mediaId = mediaId
            message.format = format
            message.thumbMediaId = thumbMediaId
            message.locationX = locationX
            message.locationY = locationY
            message.scale = scale
            message.label = label
            message.title = title
            message.description = description
            message.url = url
            message.ticket = ticket
            message.latitude = latitude
            message.longitude = longitude
            message.precision = precision
            message.recognition = recognition
        }
        return message
    }

    private fun reply(tid: Long, aid: Long, inMessage: WxMpXmlMessage): Message? {
        val reply = replyService.findCustomReply(tid, aid, inMessage)
        if (reply == null) return null
        reply.apply {
            return@reply when (messageType) {
                MessageType.text -> Message(WxConsts.XML_MSG_TEXT, text)
                MessageType.image -> Message(WxConsts.XML_MSG_IMAGE, mediaId)
                MessageType.voice -> Message(WxConsts.XML_MSG_VOICE, mediaId)
                MessageType.video -> Message(WxConsts.XML_MSG_VIDEO, mediaId, title, description)
                MessageType.music -> Message(WxConsts.XML_MSG_MUSIC, mediaId, title, description, musicUrl, hqMusicUrl)
                MessageType.news -> Message(WxConsts.XML_MSG_NEWS, mediaId)
                MessageType.plugin -> Message("plugin", pluginConfig, pluginCode)
            }
        }
        return null
    }

    private fun action(aid: Long, inMessage: WxMpXmlMessage): Message? {
        if (inMessage.eventKey.isNullOrEmpty()) return null
        val action = actionService.findByAccountIdAndKey(aid, inMessage.eventKey)
        if (action == null) return null
        action.apply {
            return@action when (actionType) {
                ActionType.html -> Message(WxConsts.XML_MSG_NEWS, params)
                ActionType.text -> Message(WxConsts.XML_MSG_TEXT, params)
                ActionType.plugin -> Message("plugin", params, pluginCode)
            }
        }
        return null
    }

    companion object {
        protected var logger = LoggerFactory.getLogger(WeixinService::class.java)
    }
}
