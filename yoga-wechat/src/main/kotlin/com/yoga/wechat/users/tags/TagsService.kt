package com.yoga.wechat.users.tags

import com.yoga.core.exception.BusinessException
import com.yoga.core.service.BaseService
import com.yoga.wechat.weixin.WeixinServiceFactory
import me.chanjar.weixin.mp.bean.tag.WxUserTag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service


@Service(value = "wechatTagsService")
open class TagsService @Autowired constructor(
        val weixinServiceFactory: WeixinServiceFactory): BaseService() {

    @Cacheable(value = "wxTagsList", keyGenerator = "wiselyKeyGenerator")
    open fun list(tenantId: Long, accountId: Long): List<WxUserTag> {
        val weixinService = weixinServiceFactory.getService(accountId)
        val list = weixinService.userTagService.tagGet()
        return list
    }
    @Cacheable(value = "wxTagsMap", keyGenerator = "wiselyKeyGenerator")
    open fun map(tenantId: Long, accountId: Long): Map<String, WxUserTag> {
        val weixinService = weixinServiceFactory.getService(accountId)
        val list = weixinService.userTagService.tagGet()
        return list.map { it.id.toString() to it }.toMap()
    }
    open fun clearCache(tenantId: Long, accountId: Long) {
        redisOperator.removePattern("com.yoga.wechat.users.tags.TagsService.*." + tenantId + "." + accountId)
    }

    open fun add(tenantId: Long, accountId: Long, name: String) {
        val weixinService = weixinServiceFactory.getService(accountId)
        val tag = weixinService.userTagService.tagCreate(name)
        if (tag == null) throw BusinessException("创建标签失败")
        clearCache(tenantId, accountId)
    }
    open fun update(tenantId: Long, accountId: Long, tagId: Long, name: String) {
        val weixinService = weixinServiceFactory.getService(accountId)
        if (!weixinService.userTagService.tagUpdate(tagId, name))
            throw BusinessException("修改标签名称失败")
        clearCache(tenantId, accountId)
    }
    open fun delete(tenantId: Long, accountId: Long, tagId: Long) {
        val weixinService = weixinServiceFactory.getService(accountId)
        if (!weixinService.userTagService.tagDelete(tagId))
            throw BusinessException("修改标签名称失败")
        clearCache(tenantId, accountId)
    }
}