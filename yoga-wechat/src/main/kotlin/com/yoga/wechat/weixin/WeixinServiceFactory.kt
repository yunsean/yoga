package com.yoga.wechat.weixin

import com.yoga.core.exception.BusinessException
import com.yoga.core.redis.RedisOperator
import com.yoga.wechat.account.AccountRepository
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class WeixinServiceFactory @Autowired constructor(
        val accountRepository: AccountRepository,
        val redisOperator: RedisOperator) {

    private val weixinServices = mutableMapOf<Long, WeixinService>()
    open fun getService(accountId: Long): WeixinService {
        val service = weixinServices.get(accountId)
        if (service != null) return service
        synchronized(weixinServices) {
            val service = weixinServices.get(accountId)
            if (service != null) return service
            val account = accountRepository.findOne(accountId)
            if (account == null) throw BusinessException("未找到该公众号")
            try {
                val config = WxMpInMemoryConfigStorage()
                config.appId = account.appId
                config.secret = account.appSecret
                config.token = account.token
                config.aesKey = account.aesKey
                val service = WeixinService(redisOperator)
                service.wxMpConfigStorage = config
                weixinServices.set(accountId, service)
                return service
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw BusinessException("公众号配置错误")
            }
        }
    }
}
