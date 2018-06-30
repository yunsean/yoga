package com.yoga.wechat.util

import com.yoga.core.exception.BusinessException
import com.yoga.core.service.BaseService
import com.yoga.wechat.sequence.SequenceNameEnum
import com.yoga.wechat.weixin.WeixinService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.annotation.PostConstruct


@Service
open class WxSyncFactory @Autowired constructor(
        val wxSyncRepository: WxSyncRepository
    ): BaseService() {

    @PostConstruct
    open fun cleanSync() {
        redisOperator.removePattern(userSyncTokenLockKey + "*")
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private fun doSync(syncIndex: Long, tenantId: Long, accountId: Long, action: WxSyncAction, runner: (syncIndex: Long, tenantId: Long, accountId: Long, action: WxSyncAction) -> Unit) {
        runner(syncIndex, tenantId, accountId, action)
    }

    open fun sync(tenantId: Long, accountId: Long, actor: String, actorId: Long, action: WxSyncAction, runner: (syncIndex: Long, tenantId: Long, accountId: Long, action: WxSyncAction) -> Unit) {
        if (!acquireSyncLock(accountId, action)) throw BusinessException("其他用户正在刷新该账号的${action.desc}")
        Thread {
            logger.info("${accountId}开始刷新${action.desc}")
            try {
                val syncIndex = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_SYNC_ID)
                val userSync = WxSync(id = syncIndex, tenantId = tenantId, accountId = accountId, actor = actor, actorId = actorId, action = action)
                wxSyncRepository.save(userSync)
                try {
                    doSync(syncIndex, tenantId, accountId, action, runner)
                    userSync.finished = true
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                userSync.endTime = Date()
                wxSyncRepository.save(userSync)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            releaseSyncLock(accountId, action)
            logger.info("${accountId}结束刷新${action.desc}")
        }.start()
    }

    private val userInfoExpire = 60 * 60 * 1000           //millis
    private val userSyncTokenLockKey = "wechat.user.sync.lock."
    open fun acquireSyncLock(accountId: Long, action: WxSyncAction): Boolean {
        val key = userSyncTokenLockKey + action.type + "." + accountId
        val expires = System.currentTimeMillis() + userInfoExpire + 1
        val expiresStr = expires.toString()
        if (redisOperator.setNX(key, expiresStr)) return true
        val currentValueStr = redisOperator.get(key)
        if (currentValueStr != null && java.lang.Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
            val oldValueStr = redisOperator.getSet(key, expiresStr)
            if (oldValueStr != null && oldValueStr == currentValueStr) return true
        }
        return false
    }
    open fun releaseSyncLock(accountId: Long, action: WxSyncAction) {
        try {
            val key = userSyncTokenLockKey + action.type + "." + accountId
            redisOperator.remove(key)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    companion object {
        protected var logger = LoggerFactory.getLogger(WeixinService::class.java)
    }
}