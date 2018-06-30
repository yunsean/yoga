package com.yoga.wechat.users.tags

import com.yoga.core.annotation.Explain
import com.yoga.user.basic.TenantDto
import com.yoga.wechat.users.UserSex
import org.hibernate.validator.constraints.NotBlank
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull

data class ListDto(
		@Explain("账号ID") var accountId: Long = 0L,
		var name: String = ""): TenantDto()

data class UsersDto(
		@field:NotNull(message = "账号ID不能为空")var accountId: Long? = null,
		@field:NotNull(message = "标签ID不能为空")var tagId: Long? = null,
		var nickname: String? = null,
		@Enumerated(EnumType.STRING) var gender: UserSex? = null): TenantDto()

data class AddDto(
		@field:NotNull(message = "账号ID不能为空")var accountId: Long? = null,
		@field:NotBlank(message = "标签名称不能为空") var name: String = ""): TenantDto()
data class UpdateDto(
		@field:NotNull(message = "账号ID不能为空")var accountId: Long? = null,
		@field:NotNull(message = "标签ID不能为空")var id: Long? = null,
		@field:NotBlank(message = "标签名称不能为空") var name: String = ""): TenantDto()
data class DeleteDto(
		@field:NotNull(message = "账号ID不能为空")var accountId: Long? = null,
		@field:NotNull(message = "标签ID不能为空")var id: Long? = null): TenantDto()

data class TagUsersDto(
		@field:NotNull(message = "账号ID不能为空")var accountId: Long? = null,
		@field:NotNull(message = "标签ID不能为空")var tagId: Long? = null,
		@field:NotEmpty(message = "用户不能为空")var openIds: Array<String>? = null): TenantDto()

