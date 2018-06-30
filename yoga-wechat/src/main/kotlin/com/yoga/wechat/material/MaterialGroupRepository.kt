package com.yoga.wechat.material

import com.yoga.core.repository.BaseRepository

interface MaterialGroupRepository : BaseRepository<MaterialGroup, Long> {

    fun findByTenantId(tenantId: Long): List<MaterialGroup>
    fun findOneByTenantIdAndId(tenantId: Long, id: Long): MaterialGroup?
}
