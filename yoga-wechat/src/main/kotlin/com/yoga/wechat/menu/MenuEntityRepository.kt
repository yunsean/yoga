package com.yoga.wechat.menu

import com.yoga.core.repository.BaseRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import javax.transaction.Transactional

interface MenuEntityRepository: BaseRepository<MenuEntity, Long> {

    fun findByMenuId(menuId: Long): List<MenuEntity>?
    fun findByMenuIdAndParentId(menuId: Long, parentId: Long): List<MenuEntity>?

    @Transactional
    @Modifying
    @Query(value = "delete from wx_menu_entity where menu_id in (select id from wx_menu where tenant_id = ?1 and account_id = ?2)", nativeQuery = true)
    fun deleteByTenantIdAndAccountId(tenantId: Long, accountId: Long);

    @Transactional
    fun deleteByMenuId(menuId: Long)
}
