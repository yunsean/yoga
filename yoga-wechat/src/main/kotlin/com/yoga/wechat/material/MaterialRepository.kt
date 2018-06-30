package com.yoga.wechat.material

import com.yoga.core.repository.BaseRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component(value = "weixinMediaRepository")
interface MaterialRepository : BaseRepository<Material, Long> {

    fun findOneByTenantIdAndAccountIdAndId(tenantId: Long, accountId: Long, id: Long): Material?

    @Transactional
    @Modifying
    @Query(value = "update wx_material set group_id = 0 where tenant_id = ?1 and group_id = ?2", nativeQuery = true)
    fun clearGroupIdByTenantIdAndGroupId(tenantId: Long, groupId: Long)

    @Transactional
    @Modifying
    fun deleteByTenantIdAndAccountIdAndTypeAndBatchIndexNot(tenantId: Long, accountId: Long, type: MaterialType, batchIndex: Long)

    fun findByTenantIdAndAccountIdAndTypeAndBatchIndexNot(tenantId: Long, accountId: Long, type: MaterialType, batchIndex: Long): List<Material>

    fun findByTenantIdAndIdIn(tenantId: Long, ids: Array<Long>): List<Material>

    fun findFirstByTenantIdAndMediaId(tenantId: Long, mediaId: String): Material?
    fun findByTenantIdAndMediaId(tenantId: Long, mediaId: String): Material?
    fun findByTenantIdAndAccountIdAndMediaIdIn(tenantId: Long, accountId: Long, mediaIds: Array<String>): List<Material>?
}
