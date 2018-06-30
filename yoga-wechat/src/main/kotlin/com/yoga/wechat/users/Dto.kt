package com.yoga.wechat.users

import com.yoga.core.annotation.Explain
import com.yoga.user.basic.TenantDto
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull

data class ListDto(
		@Explain("账号ID") var accountId: Long = 0L,
		var nickname: String? = null,
		@Enumerated(EnumType.STRING) var gender: UserSex? = null): TenantDto()
data class RefreshDto(
		@Explain("账号ID") @field:NotNull(message = "账号ID不能为空")var accountId: Long? = null): TenantDto()
data class UpdateDto(
        @field:NotNull(message = "账号ID不能为空")var accountId: Long? = null,
        @field:NotNull(message = "用户ID不能为空")var openId: String? = null,
        var remark: String = ""): TenantDto()
data class BindingDto(
		@Explain("账号ID") @field:NotNull(message = "账号ID不能为空")var accountId: Long? = null,
        @field:NotNull(message = "用户ID不能为空")var openId: String? = null): TenantDto()

data class TagsDto(
		@field:NotNull(message = "账号ID不能为空")var accountId: Long? = null,
		@field:NotNull(message = "用户ID不能为空")var openId: String? = null): TenantDto()
data class UntagDto(
		@field:NotNull(message = "账号ID不能为空")var accountId: Long? = null,
		@field:NotNull(message = "用户ID不能为空")var openId: String? = null,
		@field:NotNull(message = "标签ID不能为空")var tagId: Long? = null): TenantDto()
data class TagDto(
		@field:NotNull(message = "账号ID不能为空")var accountId: Long? = null,
		@field:NotNull(message = "用户ID不能为空")var openId: String? = null,
		@field:NotNull(message = "标签ID不能为空")var tagId: Long? = null): TenantDto()

