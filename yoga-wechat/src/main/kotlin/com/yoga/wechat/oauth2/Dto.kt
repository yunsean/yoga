package com.yoga.wechat.oauth2

import com.yoga.user.basic.TenantDto
import org.hibernate.validator.constraints.NotEmpty

data class DoBindDto(
        var aid: Long = 0,
        @field:NotEmpty(message = "请输入用户名") var username: String? = null,
        @field:NotEmpty(message = "请输入密码") var password: String? = null,
        @field:NotEmpty(message = "微信ID不能为空") var openId: String? = null,
		var uri: String? = null,
		var first: Boolean = true): TenantDto()


data class BindDto(
        var aid: Long = 0,
        var openId: String? = null,
        var uri: String? = null) : TenantDto()


