package com.yoga.wechat.reply

import com.yoga.core.cache.BaseCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
open class ReplyCache @Autowired constructor(
        val replyRepository: ReplyRepository) : BaseCache() {

    @Cacheable(value = "getReply", keyGenerator = "wiselyKeyGenerator")
    open fun getReply(tid: Long, aid: Long, msgType: String, event: String?, content: String?, tagId: Int?, gender: Gender?): Reply? {
        if (msgType == "event") {
            if (event == "subscribe") return replyRepository.findOneByTenantIdAndAccountIdAndEvent(tid, aid, Event.subscribe)
            else if (event == "unsubscribe") return replyRepository.findOneByTenantIdAndAccountIdAndEvent(tid, aid, Event.unsubscribe)
            else return null
        } else {
            if (tagId != null && gender != null) {
                if (msgType == "text" && !content.isNullOrBlank()) {
                    val replies = replyRepository.findByTenantIdAndAccountIdAndEventAndTagAndGender(tid, aid, Event.keyword, tagId, gender)
                    replies.forEach { reply ->
                        val keyword = reply.keyword?.split(",")
                        keyword?.forEach { if (content!!.contains(it)) return reply }
                    }
                }
            }
            if (msgType == "text" && !content.isNullOrBlank()) {
                val replies = replyRepository.findByTenantIdAndAccountIdAndEventAndTagAndGender(tid, aid, Event.keyword, 0, Gender.unknown)
                replies.forEach { reply ->
                    val keyword = reply.keyword?.split(",")
                    keyword?.forEach { if (content!!.contains(it)) return reply }
                }
            }

            val event = when (msgType) {
                "text" -> Event.text
                "image" -> Event.image
                "voice" -> Event.voice
                "video" -> Event.video
                "shortvideo" -> Event.shortvideo
                "location" -> Event.location
                else -> return null
            }
            if (tagId != null && gender != null) {
                val reply = replyRepository.findFirstByTenantIdAndAccountIdAndEventAndTagAndGender(tid, aid, event, tagId, gender)
                if (reply != null) return reply
            }
            if (true) {
                val reply = replyRepository.findFirstByTenantIdAndAccountIdAndEventAndTagAndGender(tid, aid, event, 0, Gender.unknown)
                if (reply != null) return reply
            }

            if (tagId != null && gender != null) {
                val reply = replyRepository.findFirstByTenantIdAndAccountIdAndEventAndTagAndGender(tid, aid, Event.common, tagId, gender)
                if (reply != null) return reply
            }
            if (true) {
                val reply = replyRepository.findFirstByTenantIdAndAccountIdAndEventAndTagAndGender(tid, aid, Event.common, 0, Gender.unknown)
                if (reply != null) return reply
            }

            return null
        }
    }
}