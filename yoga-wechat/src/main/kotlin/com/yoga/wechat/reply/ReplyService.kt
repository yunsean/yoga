package com.yoga.wechat.reply

import com.yoga.core.data.PageList
import com.yoga.core.exception.BusinessException
import com.yoga.core.service.BaseService
import com.yoga.wechat.account.AccountRepository
import com.yoga.wechat.sequence.SequenceNameEnum
import com.yoga.wechat.users.UserRepository
import com.yoga.wechat.users.UserSex
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class ReplyService @Autowired constructor(
        val replyRepository: ReplyRepository,
        val accountRepository: AccountRepository,
        val userRepository: UserRepository,
        val replyCache: ReplyCache) : BaseService() {

    @Transactional
    open fun sync(tenantId: Long, accountId: Long, actor: String, actorId: Long) {
        val account = accountRepository.findOneByTenantIdAndId(tenantId, accountId)
        if (account == null) throw BusinessException("公众号不存在")
        //没找到接口
    }

    open fun add(tenantId: Long, accountId: Long, event: Event, tag: Int?, gender: Gender?, name: String, keyword: String?,
                 type: MessageType, text: String, mediaId: String, mediaName: String, title: String, description: String,
                 musicUrl: String, hqMusicUrl: String, pluginCode: String, pluginConfig: String) {
        val reply = Reply(0, tenantId, accountId, event, tag ?: 0, gender ?: Gender.unknown, name, keyword, type, text, mediaId, mediaName, title, description, musicUrl, hqMusicUrl, pluginCode, pluginConfig)
        reply.id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_REPLY_ID)
        replyRepository.save(reply)
    }

    open fun update(tenantId: Long, accountId: Long, id: Long, event: Event?, tag: Int?, gender: Gender?, name: String, keyword: String?,
                    type: MessageType?, text: String?, mediaId: String?, mediaName: String?, title: String, description: String?,
                    musicUrl: String?, hqMusicUrl: String?, pluginCode: String, pluginConfig: String) {
        val reply = replyRepository.findOneByTenantIdAndAccountIdAndId(tenantId, accountId, id)
        if (reply == null) throw BusinessException("自定义回复不存在")
        if (event != null) reply.event = event
        if (gender != null) reply.gender = gender
        if (!name.isNullOrBlank()) reply.name = name
        if (keyword != null) reply.keyword = keyword
        if (type != null) reply.messageType = type
        if (!text.isNullOrBlank()) reply.text = text
        if (!mediaId.isNullOrBlank()) reply.mediaId = mediaId
        if (!mediaName.isNullOrBlank()) reply.mediaName = mediaName
        if (!title.isNullOrBlank()) reply.title = title
        if (!description.isNullOrBlank()) reply.description = description
        if (!musicUrl.isNullOrBlank()) reply.musicUrl = musicUrl
        if (!hqMusicUrl.isNullOrBlank()) reply.hqMusicUrl = hqMusicUrl
        if (!pluginCode.isNullOrBlank()) reply.pluginCode = pluginCode
        if (!pluginConfig.isNullOrBlank()) reply.pluginConfig = pluginConfig
        replyRepository.save(reply)
    }

    open fun delete(tenantId: Long, accountId: Long, id: Long) {
        val reply = replyRepository.findOneByTenantIdAndAccountIdAndId(tenantId, accountId, id)
        if (reply == null) throw BusinessException("自定义回复不存在")
        replyRepository.delete(reply)
    }

    open fun get(tenantId: Long, accountId: Long, id: Long): Reply {
        val reply = replyRepository.findOneByTenantIdAndAccountIdAndId(tenantId, accountId, id)
        if (reply == null) throw BusinessException("自定义回复不存在")
        return reply
    }

    open fun list(tenantId: Long, accountId: Long, name: String?, event: Event?, pageIndex: Int, pageSize: Int): PageList<Reply> {
        val request = PageRequest(pageIndex, pageSize, Sort.Direction.DESC, "id")
        var result = replyRepository.findAll({ root, _, cb ->
            val predicate = cb.conjunction()
            val expressions = predicate.expressions
            expressions.add(cb.equal(root.get<Long>("tenantId"), tenantId))
            expressions.add(cb.equal(root.get<Long>("accountId"), accountId))
            if (!name.isNullOrBlank()) expressions.add(cb.like(root.get<String>("name"), "%" + name + "%"))
            if (event != null) expressions.add(cb.equal(root.get<UserSex>("event"), event))
            predicate
        }, request)
        return PageList(result)
    }

    open fun findCustomReply(tid: Long, aid: Long, inMessage: WxMpXmlMessage): Reply? {
        val user = userRepository.findOneByTenantIdAndAccountIdAndOpenId(tid, aid, inMessage.fromUser)
        val tagId = if (user == null || user.groupId  == null) null else user.groupId!!
        val gender = if (user == null) null else Gender.getEnum(user.sex.toString())
        return replyCache.getReply(tid, aid, inMessage.msgType, inMessage.event, inMessage.content, tagId, gender)
    }
}