package com.yoga.wechat.users

import com.yoga.core.repository.BaseRepository
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component(value = "weixinUserBindingRepository")
interface UserBindingRepository : BaseRepository<UserBinding, UserBindingPK> {
    @Transactional
    fun deleteByAccountIdAndOpenId(accountId: Long, openId: String)
}