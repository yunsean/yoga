package com.yoga.wechat.actions

import com.yoga.core.interfaces.wechat.EventType
import com.yoga.user.basic.TenantDto
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull

internal data class ListDto(
        @Enumerated(EnumType.STRING) var event: EventType? = null) : TenantDto()

data class ConfigDto(
        @field:NotNull(message = "插件CODE不能为空") var code: String? = null,
        var config: String? = null) : TenantDto()