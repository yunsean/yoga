package com.yoga.wechat.material

import com.yoga.core.repository.BaseRepository

interface MaterialExRepository : BaseRepository<MaterialEx, Long> {
    fun findOneByTenantIdAndMediaId(tenantId: Long, mediaId: String): MaterialEx?
    fun findByTenantIdAndMediaIdIn(tenantId: Long, mediaIds: List<String>): List<MaterialEx>
}
