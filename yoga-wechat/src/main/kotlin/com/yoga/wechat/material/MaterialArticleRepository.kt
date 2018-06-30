package com.yoga.wechat.material

import com.yoga.core.repository.BaseRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
interface MaterialArticleRepository : BaseRepository<MaterialArticle, Long> {

    @Transactional
    @Modifying
    fun deleteByTenantIdAndAccountIdAndBatchIndexNot(tenantId: Long, accountId: Long, batchIndex: Long)

    @Transactional
    @Modifying
    fun deleteByTenantIdAndMaterialId(tenantId: Long, materialId: Long)

    fun findByTenantIdAndAccountIdAndMaterialId(tenantId: Long, accountId: Long, materialId: Long): List<MaterialArticle>?
    fun findByTenantIdAndAccountIdAndId(tenantId: Long, accountId: Long, id: Long): MaterialArticle?
    fun findByTenantIdAndId(tenantId: Long, id: Long): MaterialArticle?
    fun countByTenantIdAndMaterialId(tenantId: Long, materialId: Long): Long
}
