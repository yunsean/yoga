package com.yoga.wechat.users

import com.yoga.core.data.PageList
import com.yoga.core.exception.BusinessException
import com.yoga.core.service.BaseService
import com.yoga.wechat.account.AccountRepository
import com.yoga.wechat.sequence.SequenceNameEnum
import com.yoga.wechat.users.tags.TagsService
import com.yoga.wechat.util.WxSyncAction
import com.yoga.wechat.util.WxSyncFactory
import com.yoga.wechat.weixin.WeixinServiceFactory
import me.chanjar.weixin.mp.bean.result.WxMpUser
import me.chanjar.weixin.mp.bean.tag.WxUserTag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import javax.persistence.EntityManagerFactory


@Service(value = "wechatUserService")
open class UserService @Autowired constructor(
        val entityManagerFactory: EntityManagerFactory,
        val syncFactory: WxSyncFactory,
        val userRepository: UserRepository,
        val userBindingRepository: UserBindingRepository,
        val accountRepository: AccountRepository,
        val weixinServiceFactory: WeixinServiceFactory,
        val tagsService: TagsService,
        val jdbcTemplate: JdbcTemplate): BaseService() {

    open fun sync(tenantId: Long, accountId: Long, actor: String, actorId: Long) {
        val account = accountRepository.findOneByTenantIdAndId(tenantId, accountId)
        if (account == null) throw BusinessException("公众号不存在")
        syncFactory.sync(tenantId, accountId, actor, actorId, WxSyncAction.user) {syncIndex, tenantId, accountId, _ ->
            val service = weixinServiceFactory.getService(accountId)
            jdbcTemplate.update("set names utf8mb4;")
            var totalCount = 0;
            var nextOpenId: String? = null
            while (true) {
                logger.info("开始拉取用户信息，从${nextOpenId}开始")
                val wxUsers = service.userService.userList(nextOpenId)
                val batchCount = (wxUsers.count + 99) / 100
                logger.info("拉取到 ${wxUsers.count} 条记录")
                for (batch in 0 until batchCount) {
                    val sub = wxUsers.openids.subList(batch * 100, minOf(batch * 100 + 100, wxUsers.count))
                    val infos = service.userService.userInfoList(sub)
                    val users = mutableListOf<User>()
                    infos.forEach {
                        val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_USER_ID)
                        users.add(User(id, tenantId, accountId, syncIndex, it))
                    }
                    userRepository.save(users)
                }
                totalCount += wxUsers.count
                if (totalCount >= wxUsers.total) break
                else nextOpenId = wxUsers.nextOpenid
                logger.info("共拉取到 ${totalCount} 条记录")
            }
            userRepository.deleteByTenantIdAndAccountIdAndNotBatchIndex(tenantId, accountId, syncIndex)
        }
    }

    open fun get(tenantId: Long, accountId: Long, openId: String?): User? {
        if (openId == null) return null
        return userRepository.findOneByTenantIdAndAccountIdAndOpenId(tenantId, accountId, openId)
    }
    open fun add(tenantId: Long, accountId: Long, userInfo: WxMpUser?): User? {
        if (userInfo == null) return null
        val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_USER_ID)
        val user = User(id, tenantId, accountId, 0, userInfo)
        userRepository.save(user)
        return user
    }

    open fun list(tenantId: Long, accountId: Long, nickname: String?, tagId: Long?, gender: UserSex?, pageIndex: Int, pageSize: Int): PageList<User> {
        val request = PageRequest(pageIndex, pageSize, Sort.Direction.DESC, "id")
        val users = userRepository.findAll({ root, _, cb ->
            val predicate = cb.conjunction()
            val expressions = predicate.expressions
            expressions.add(cb.equal(root.get<Long>("tenantId"), tenantId))
            expressions.add(cb.equal(root.get<Long>("accountId"), accountId))
            if (!nickname.isNullOrBlank()) expressions.add(cb.equal(root.get<String>("nickname"), nickname))
            if (tagId != null) expressions.add(cb.like(root.get<String>("tagIdList"), "%|" + tagId + "|%"))
            if (gender != null) expressions.add(cb.equal(root.get<UserSex>("sex"), gender))
            predicate
        }, request)
        return PageList(users)
    }
    open fun listEx(tenantId: Long, accountId: Long, nickname: String?, tagId: Long?, gender: UserSex?, pageIndex: Int, pageSize: Int): PageList<BindUser> {
        val em = entityManagerFactory.createEntityManager()
        val sql = "select u.fullname, u.username, u.id as local_user_id, wx.* from wx_users wx left join wx_user_binding ub on ub.open_id = wx.open_id left join s_user u on u.id = ub.local_user_id" +
                    " where wx.tenant_id = ?1 and wx.account_id = ?2 and (wx.sex = ?3 or ISNULL(?3))" +
                    " and (wx.nickname like ?4 or ISNULL(?5)) and (wx.tagid_list LIKE ?6 or ISNULL(?7))"
        try {
            var query = em.createNativeQuery(sql, "ReturnWxUserBinding")
                    .setParameter(1, tenantId)
                    .setParameter(2, accountId)
                    .setParameter(3, gender?.toString())
                    .setParameter(4, "%" + nickname + "%")
                    .setParameter(5, nickname)
                    .setParameter(6, "%|" + tagId + "|%")
                    .setParameter(7, tagId)
            val totalCount = query.resultList.size
            val resultList: List<BindUser> = if (pageIndex * pageSize < totalCount) {
                query.setFirstResult(pageIndex * pageSize)
                query.setMaxResults(pageSize)
                query.resultList as List<BindUser>
            } else {
                listOf()
            }
            return PageList(resultList, pageIndex, pageSize, totalCount)
        } finally {
            em.close()
        }
    }

    open fun getBinding(tenantId: Long, accountId: Long, openId: String) : BindUser? {
        val em = entityManagerFactory.createEntityManager()
        val sql = "select u.fullname, u.username, u.id as local_user_id, wx.* from wx_users wx left join wx_user_binding ub on ub.open_id = wx.open_id left join s_user u on u.id = ub.local_user_id" +
                " where wx.tenant_id = ?1 and wx.account_id = ?2 and ub.open_id = ?3"
        try {
            var query = em.createNativeQuery(sql, "ReturnWxUserBinding")
                    .setParameter(1, tenantId)
                    .setParameter(2, accountId)
                    .setParameter(3, openId)
            val resultList = query.resultList as List<BindUser>?
            if (resultList == null || resultList.size < 1) return null
            return resultList.get(0)
        } finally {
            em.close()
        }
    }

    open fun getBindUser(accountId: Long, openId: String): com.yoga.user.user.model.User? {
        val em = entityManagerFactory.createEntityManager()
        val sql = "select u.* from s_user u " +
                "left join wx_user_binding ub on u.id = ub.local_user_id " +
                " where ub.account_id = ?1 and ub.open_id = ?2"
        try {
            var query = em.createNativeQuery(sql, "ReturnUser")
                    .setParameter(1, accountId)
                    .setParameter(2, openId)
            val resultList = query.resultList as List<com.yoga.user.user.model.User>?
            if (resultList == null || resultList.size < 1) return null
            return resultList.get(0)
        } finally {
            em.close()
        }
    }

    open fun bind(tenantId: Long, userId: Long, accountId: Long, openId: String) {
        if (openId.isBlank()) throw IllegalArgumentException("OpenId不能为空")
        val userBinding = UserBinding(tenantId, userId, accountId, openId)
        userBindingRepository.save(userBinding)
    }
    open fun unbind(tenantId: Long, userId: Long) {
        userBindingRepository.delete(UserBindingPK(tenantId, userId))
    }
    open fun unbind(accountId: Long, openId: String) {
        userBindingRepository.deleteByAccountIdAndOpenId(accountId, openId)
    }

    open fun tags(tenantId: Long, accountId: Long, openId: String): List<WxUserTag?> {
        val user = userRepository.findOneByTenantIdAndAccountIdAndOpenId(tenantId, accountId, openId)
        val tagIds = user?.tagIdList?.let { it.trim('|').split("|").toMutableList() } ?: mutableListOf()
        val allTags = tagsService.map(tenantId, accountId)
        return tagIds.mapNotNull { allTags.get(it) }
    }
    open fun removeTag(tenantId: Long, accountId: Long, tagId: Long, openIds: Array<String>) {
        val weixinService = weixinServiceFactory.getService(accountId)
        if (!weixinService.userTagService.batchUntagging(tagId, openIds))
            throw BusinessException("删除用户标签失败")
        val users = userRepository.findByTenantIdAndAccountIdAndOpenIdIn(tenantId, accountId, openIds)
        users.forEach {
            var tagIds = it.tagIdList?.let { it.trim('|').split("|").toMutableList() } ?: mutableListOf()
            tagIds.remove(tagId.toString())
            it.setTagIdList(tagIds)
        }
        userRepository.save(users)
    }
    open fun addTag(tenantId: Long, accountId: Long, tagId: Long, openIds: Array<String>) {
        val weixinService = weixinServiceFactory.getService(accountId)
        if (!weixinService.userTagService.batchTagging(tagId, openIds))
            throw BusinessException("添加用户标签失败")
        val users = userRepository.findByTenantIdAndAccountIdAndOpenIdIn(tenantId, accountId, openIds)
        users.forEach {
            var tagIds = it.tagIdList?.let { it.trim('|').split("|").toMutableList() } ?: mutableListOf()
            tagIds.add(tagId.toString())
            it.setTagIdList(tagIds)
        }
        userRepository.save(users)
    }

    open fun update(tenantId: Long, accountId: Long, openId: String, remark: String) {
        val user = userRepository.findOneByTenantIdAndAccountIdAndOpenId(tenantId, accountId, openId)
        if (user == null) throw BusinessException("未找到该用户")
        val weixinService = weixinServiceFactory.getService(accountId)
        weixinService.userService.userUpdateRemark(openId, remark)
        user.remark = remark
        userRepository.save(user)
    }
}