package com.yoga.wechat.account

import com.yoga.core.annotation.Explain
import com.yoga.user.basic.TenantDto
import org.hibernate.validator.constraints.NotBlank
import javax.validation.constraints.NotNull

internal data class AddDto(
		@field:NotBlank(message = "账号名称不能为空")var name: String = "",
		@field:NotBlank(message = "Token不能为空")var token: String = "",
		@field:NotBlank(message = "微信号不能为空")var number: String = "",
		@field:NotBlank(message = "原始ID不能为空")var rawId: String = "",
		@field:NotBlank(message = "appId不能为空")var appId: String = "",
		@field:NotBlank(message = "appSecret不能为空")var appSecret: String = "",
		@field:NotBlank(message = "aesKey不能为空")var aesKey: String = "",
		var remark: String = ""): TenantDto()


internal data class DelDto(
		@Explain("账号ID") @field:NotNull(message = "账号ID不能为空")var id: Long? = null): TenantDto()

internal data class GetDto(
		@Explain("账号ID") @field:NotNull(message = "账号ID不能为空")var id: Long? = null): TenantDto()

internal data class ListDto(
        var name: String? = null,
        var filter: String? = null): TenantDto()

internal data class UpdateDto(
		@Explain("账号ID") @field:NotNull(message = "频道ID不能为空")var id: Long? = null,
		var name: String? = null,
		var token: String? = null,
		var number: String? = null,
		var appSecret: String? = null,
		var aesKey: String? = null,
		var remark: String? = null): TenantDto()

internal data class ListMenuDto(
		@Explain("账号ID") @field:NotNull(message = "账号ID不能为空")var id: Long? = null): TenantDto()

