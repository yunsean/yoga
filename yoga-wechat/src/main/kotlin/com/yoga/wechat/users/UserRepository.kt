package com.yoga.wechat.users

import com.yoga.core.repository.BaseRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component(value = "weixinUserRepository")
interface UserRepository: BaseRepository<User, Long> {

    @Transactional
    @Modifying
    @Query(value = "delete from wx_users where tenant_id = ?1 and account_id = ?2 and batch_index != ?3", nativeQuery = true)
    fun deleteByTenantIdAndAccountIdAndNotBatchIndex(tenantId: Long, accountId: Long, batchIndex: Long)

    fun findOneByTenantIdAndAccountIdAndOpenId(tenantId: Long, accountId: Long, openId: String): User?
    fun findOneByAccountIdAndOpenId(accountId: Long, openId: String): User?
    fun findByTenantIdAndAccountIdAndOpenIdIn(tenantId: Long, accountId: Long, openIds: Array<String>): List<User>
}
