package com.yoga.wechat.weixin

import com.yoga.core.service.BaseService
import com.yoga.core.utils.StrUtil
import com.yoga.wechat.material.MaterialArticleRepository
import com.yoga.wechat.sequence.SequenceNameEnum
import me.chanjar.weixin.common.api.WxConsts
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ActionService @Autowired constructor(
        var materialArticleRepository: MaterialArticleRepository,
        var actionRepository: ActionRepository ) : BaseService() {

    open fun findByAccountIdAndKey(accountId: Long, eventKey: String): Action? {
        val action = actionRepository.findByKey(eventKey)
        if (action == null || action.accountId != accountId) return null
        return action
    }

    open fun deleteByAccountId(accountId: Long) {
        actionRepository.deleteByAccountId(accountId)
    }

    open fun add(tenantId: Long, accountId: Long, actionType: ActionType, pluginCode: String?, params: String?): String {
        val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_ACTION_ID)
        val key = "wxk_$id"
        val action = Action(id, tenantId, accountId, key, actionType, pluginCode, params)
        actionRepository.save(action)
        return key
    }

    open fun sendCustomMessage(message: Message, tid: Long, aid: Long): WxMpXmlOutMessage? {
        if (StrUtil.isEmpty(message.openid)) WeixinService.logger.error("customMessage's openid should not be null")
        var type = message.type
        if (StrUtil.isEmpty(type)) type = WxConsts.XML_MSG_TEXT
        return try {
            if (type == WxConsts.XML_MSG_IMAGE) {
                WxMpXmlOutMessage
                        .IMAGE()
                        .toUser(message.openid)
                        .fromUser(message.fromUser)
                        .mediaId(message.content)
                        .build()
            } else if (type == WxConsts.XML_MSG_VOICE) {
                WxMpXmlOutMessage
                        .VOICE()
                        .toUser(message.openid)
                        .fromUser(message.fromUser)
                        .mediaId(message.content)
                        .build()
            } else if (type == WxConsts.XML_MSG_VIDEO) {
                WxMpXmlOutMessage
                        .VIDEO()
                        .toUser(message.openid)
                        .fromUser(message.fromUser)
                        .mediaId(message.content)
                        .title(message.title)
                        .description(message.description)
                        .build()
            } else if (type == WxConsts.XML_MSG_MUSIC) {
                WxMpXmlOutMessage
                        .MUSIC()
                        .toUser(message.openid)
                        .fromUser(message.fromUser)
                        .musicUrl(message.musicUrl)
                        .hqMusicUrl(message.hqMusicUrl)
                        .title(message.title)
                        .description(message.description)
                        .thumbMediaId(message.content)
                        .build()
            } else if (type == WxConsts.XML_MSG_NEWS) {
                val exists = materialArticleRepository.findByTenantIdAndAccountIdAndMaterialId(tid, aid, message.content!!.toLong())
                if (exists != null) {
                    val newsBuilder = WxMpXmlOutMessage.NEWS()
                            .toUser(message.openid)
                            .fromUser(message.fromUser)
                    exists?.forEach() {
                        val article = WxMpXmlOutNewsMessage.Item()
                        article.title = it.title
                        article.description = it.digest
                        article.picUrl = it.thumbUrl
                        article.url = it.clickUrl
                        newsBuilder.addArticle(article)
                    }
                    newsBuilder.build()
                } else {
                    null
                }
            } else if (type == WxConsts.XML_MSG_TEXT && !message.content.isNullOrEmpty()) {
                WxMpXmlOutMessage
                        .TEXT()
                        .toUser(message.openid)
                        .fromUser(message.fromUser)
                        .content(message.content)
                        .build()
            } else {
                null
            }
        } catch (e: WxErrorException) {
            null
        }
    }

}
