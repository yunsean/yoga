package com.yoga.wechat.account

import com.yoga.core.data.PageList
import com.yoga.core.exception.BusinessException
import com.yoga.core.service.BaseService
import com.yoga.tenant.setting.service.SettingService
import com.yoga.wechat.sequence.SequenceNameEnum
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


@Service
open class AccountService @Autowired constructor(
        val accountRepository: AccountRepository,
        val settingService: SettingService): BaseService() {

    open fun getAccessToken(token: String, appId: String, appSecret: String): String {
        try {
            val config = WxMpInMemoryConfigStorage()
            config.appId = appId
            config.secret = appSecret
            config.token = token
            val wxService = WxMpServiceImpl()
            wxService.wxMpConfigStorage = config
            val accessToken = wxService.accessToken
            return accessToken
        } catch (ex: Exception) {
            throw BusinessException(ex.message)
        }
    }

    open fun add(tenantId: Long, name: String, token: String, number: String, rawId: String, appId: String, appSecret: String, aesKey: String, remark: String?) {
        val maxCount = maxAccountCount(tenantId)
        if (maxCount > 0 && accountRepository.countByTenantId(tenantId) >= maxCount) throw BusinessException("最多只能管理${maxCount}个微信公众号")
        var current = accountRepository.findFirstByTenantIdAndName(tenantId, name)
        if (current != null) throw BusinessException("已存在同名账号")
        current = accountRepository.findFirstByTenantIdAndAppId(tenantId, appId)
        if (current != null) throw BusinessException("已添加该微信号")
        current = accountRepository.findOneByTenantIdAndRawId(tenantId, rawId)
        if (current != null) throw BusinessException("你或者其他人已经添加过该微信号")
        val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_ACCOUNT_ID)
        val accessToken = getAccessToken(token, appId, appSecret)
        val account = Account(id, tenantId, name, token, number, rawId, appId, appSecret, aesKey, remark, accessToken)
        accountRepository.save(account)
        clearCache(tenantId)
    }

    open fun delete(tenantId: Long, id: Long) {
        val channel = accountRepository.findOneByTenantIdAndId(tenantId, id) ?: throw BusinessException("账号不存在")
        accountRepository.delete(channel)
        clearCache(tenantId)
    }

    open fun get(tenantId: Long, id: Long): Account {
        val account = accountRepository.findOne(id) ?: throw BusinessException("账号不存在")
        if (account.tenantId != tenantId) throw BusinessException("账号不存在")
        return account
    }

    open fun get(id: Long): Account {
        val account = accountRepository.findOne(id) ?: throw BusinessException("账号不存在")
        return account
    }

    open fun update(tenantId: Long, accountId: Long, name: String?, token: String?, number: String?, appSecret: String?, aesKey: String?, remark: String?) {
        var saved = accountRepository.findOneByTenantIdAndId(tenantId, accountId) ?: throw BusinessException("账号不存在")
        if (name != null && name.isNotBlank()) {
            val channel = accountRepository.findFirstByTenantIdAndName(tenantId, name)
            if (channel != null && channel.id !== accountId) throw BusinessException("已存在同名账号")
            saved.name = name
        }
        if (token != null && token.isNotBlank()) saved.token = token
        if (number != null && number.isNotBlank()) saved.number = number
        if (appSecret != null && appSecret.isNotBlank()) saved.appSecret = appSecret
        if (aesKey != null && aesKey.isNotBlank()) saved.aesKey = aesKey
        if (remark != null) saved.remark = remark
        accountRepository.save(saved)
        clearCache(tenantId)
    }

    @Cacheable(value = "allAccount", keyGenerator = "wiselyKeyGenerator")
    open fun all(tenantId: Long): List<Account> {
        val accounts = accountRepository.findByTenantId(tenantId)
        return accounts?.let { return@all it } ?: arrayListOf()
    }
    open fun clearCache(tenantId: Long) {
        redisOperator.removePattern("com.yoga.wechat.account.AccountService.*." + tenantId)
    }

    open fun list(tenantId: Long, name: String?, filter: String?, pageIndex: Int, pageSize: Int): PageList<Account> {
        val request = PageRequest(pageIndex, pageSize, Sort.Direction.DESC, "id")
        val accounts = accountRepository.findAll({ root, _, cb ->
            val predicate = cb.conjunction()
            val expressions = predicate.expressions
            expressions.add(cb.equal(root.get<Long>("tenantId"), tenantId))
            if (!name.isNullOrBlank()) expressions.add(cb.like(root.get<String>("name"), "%$name%"))
            if (!filter.isNullOrBlank()) expressions.add(cb.or(cb.like(root.get<String>("number"), "%$filter%"),
                    cb.like(root.get<String>("appId"), "%$filter%"),
                    cb.like(root.get<String>("appSecret"), "%$filter%")))
            predicate
        }, request)
        return PageList(accounts)
    }

    open fun getWebIP(): String? {
        try {
            val url = URL("http://ip.chinaz.com/getip.aspx")
            val urlConn = url.openConnection()
            urlConn.connectTimeout = 2000
            urlConn.readTimeout = 1000
            urlConn.allowUserInteraction = false
            urlConn.doOutput = true
            val br = BufferedReader(InputStreamReader(urlConn.getInputStream()))
            val sb = StringBuffer("")
            var line = br.readLine()
            while (line != null) {
                sb.append(line + "\r\n")
                line = br.readLine()
            }
            br.close()
            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    open fun maxAccountCount(tenantId: Long): Int {
        return settingService.get(tenantId, Module_Name, MaxAccount_key, 1)
    }
    companion object {
        const val Module_Name = "wx_account"
        const val MaxAccount_key = "wechat.account.mac.count"
    }
}