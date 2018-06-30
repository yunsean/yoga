package com.yoga.wechat.account

import com.yoga.core.repository.BaseRepository

interface AccountRepository: BaseRepository<Account, Long> {

    fun findByTenantId(tenantId: Long): List<Account>?
    fun findOneByTenantIdAndId(tenantId: Long, id: Long): Account?
    fun findOneByTenantIdAndRawId(tenantId: Long, rawId: String): Account?
    fun findFirstByTenantIdAndName(tenantId: Long, name: String): Account?
    fun findFirstByTenantIdAndAppId(tenantId: Long, appId: String): Account?
    fun countByTenantId(tenantId: Long): Long
}

