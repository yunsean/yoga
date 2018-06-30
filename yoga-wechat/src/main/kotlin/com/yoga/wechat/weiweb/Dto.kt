package com.yoga.wechat.weiweb

import com.yoga.core.annotation.Explain
import com.yoga.user.basic.TenantDto
import javax.validation.constraints.NotNull

/** Created on 2017/8/22 **/
data class GetJSConfigDto(
        @Explain("账户ID") @field:NotNull(message = "账户ID不能为空") var accountId: Long? = null,
        @Explain("URL地址") @field:NotNull(message = "URL地址不能为空") var url: String? = null) : TenantDto()


data class WKGetJSConfigDto(
        @Explain("账户ID") @field:NotNull(message = "账户ID不能为空") var accountId: Long? = null,
        @Explain("wk用户端tid") @field:NotNull(message = "sTid不能为空") var sTid: Long? = null,
        @Explain("URL地址") @field:NotNull(message = "URL地址不能为空") var url: String? = null) : TenantDto()


data class RedirectDto(
        @Explain("URL") @field:NotNull(message = "URL不能为空") var url: String? = null,
        @Explain("账户ID") @field:NotNull(message = "账户ID不能为空") var accountId: Long? = null) : TenantDto()
