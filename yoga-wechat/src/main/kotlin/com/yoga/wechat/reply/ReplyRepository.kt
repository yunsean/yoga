package com.yoga.wechat.reply

import com.yoga.core.repository.BaseRepository

interface ReplyRepository : BaseRepository<Reply, Long> {
    fun findByTenantIdAndAccountId(tenantId: Long, accountId: Long): List<Reply>
    fun findOneByTenantIdAndAccountIdAndEvent(tenantId: Long, accountId: Long, event: Event): Reply?
    fun findOneByTenantIdAndAccountIdAndId(tenantId: Long, accountId: Long, id: Long): Reply?
    fun findByTenantIdAndAccountIdAndEventAndTagAndGender(tid: Long, aid: Long, event: Event, tagId: Int, gender: Gender): List<Reply>
    fun findFirstByTenantIdAndAccountIdAndEventAndTagAndGender(tid: Long, aid: Long, event: Event, tagId: Int, gender: Gender): Reply?
}
