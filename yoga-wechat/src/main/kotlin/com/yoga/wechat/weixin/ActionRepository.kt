package com.yoga.wechat.weixin

import com.yoga.core.repository.BaseRepository
import javax.transaction.Transactional

interface ActionRepository : BaseRepository<Action, Long> {
    fun findByKey(key: String): Action?

    @Transactional
    fun deleteByKey(key: String?) {}

    @Transactional
    fun deleteByAccountId(accountId: Long)
}