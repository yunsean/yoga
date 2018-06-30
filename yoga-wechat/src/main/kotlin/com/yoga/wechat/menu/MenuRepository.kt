package com.yoga.wechat.menu

import com.yoga.core.repository.BaseRepository
import javax.transaction.Transactional

interface MenuRepository: BaseRepository<Menu, Long> {
    fun findFirstByTenantIdAndAccountIdAndIsDefault(tenantId: Long, accountId: Long, isDefault: Boolean): Menu?
    fun findOneByTenantIdAndId(tenantId: Long, id: Long): Menu?

    fun findByTenantIdAndAccountId(tenantId: Long, accountId: Long): List<Menu>?
    @Transactional
    fun deleteByTenantIdAndAccountId(tenantId: Long, accountId: Long)
}
