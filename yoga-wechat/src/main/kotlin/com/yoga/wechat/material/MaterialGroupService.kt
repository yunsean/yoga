package com.yoga.wechat.material

import com.yoga.core.data.PageList
import com.yoga.core.exception.BusinessException
import com.yoga.core.service.BaseService
import com.yoga.wechat.sequence.SequenceNameEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import javax.persistence.EntityManagerFactory
import javax.transaction.Transactional


@Service
open class MaterialGroupService @Autowired constructor(
        val entityManagerFactory: EntityManagerFactory,
        val materialGroupRepository: MaterialGroupRepository,
        val materialRepository: MaterialRepository): BaseService() {


    open fun list(tenantId: Long, name: String?, pageIndex: Int, pageSize: Int): PageList<MaterialGroup> {
        val request = PageRequest(pageIndex, pageSize, Sort.Direction.DESC, "id")
        val medias = materialGroupRepository.findAll({ root, _, cb ->
            val predicate = cb.conjunction()
            val expressions = predicate.expressions
            expressions.add(cb.equal(root.get<Long>("tenantId"), tenantId))
            if (!name.isNullOrBlank()) expressions.add(cb.equal(root.get<String>("name"), name))
            predicate
        }, request)
        return PageList(medias)
    }

    open fun list2(tenantId: Long, accountId: Long, type: MaterialType): List<MaterialGroupCount> {
        val em = entityManagerFactory.createEntityManager()
        var sql = "select mg.*, (select count(*) from wx_material_ex me, wx_material m where me.group_id = mg.id and m.media_id = me.media_id and m.type = ?1 and m.tenant_id = ?3 and m.account_id = ?2) as count from wx_material_group mg where mg.tenant_id = ?3 order by count desc"
        try {
            var query = em.createNativeQuery(sql, "ReturnWxMaterialGroupCount")
                    .setParameter(1, type.toString())
                    .setParameter(2, accountId)
                    .setParameter(3, tenantId)
            return query.resultList as List<MaterialGroupCount>
        } finally {
            em.close()
        }
    }

    open fun add(tenantId: Long, name: String, remark: String?) {
        val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MATERIAL_GROUP_ID)
        val group = MaterialGroup(id, tenantId, name, remark)
        materialGroupRepository.save(group)
    }
    open fun update(tenantId: Long, id: Long, name: String?, remark: String?) {
        val material = materialGroupRepository.findOneByTenantIdAndId(tenantId, id)
        if (material == null) throw BusinessException("素材组不存在")
        name?.let { material.name = it }
        remark?.let { material.remark = it }
        materialGroupRepository.save(material)
    }
    @Transactional
    open fun delete(tenantId: Long, id: Long) {
        val material = materialGroupRepository.findOneByTenantIdAndId(tenantId, id)
        if (material == null) throw BusinessException("素材组不存在")
        materialGroupRepository.delete(material)
        //TODO: wx_material 表中 group_id 字段？？？
//        materialRepository.clearGroupIdByTenantIdAndGroupId(tenantId, id)
    }
}