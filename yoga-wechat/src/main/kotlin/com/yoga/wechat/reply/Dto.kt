package com.yoga.wechat.reply

import com.yoga.core.annotation.Explain
import com.yoga.user.basic.TenantDto
import org.hibernate.validator.constraints.NotBlank
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull

internal data class AddDto(
        @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @field:NotNull(message = "回复事件不能为空") @Enumerated(EnumType.STRING) var event: Event? = null,
        var tag: Int? = null,
        @Enumerated(EnumType.STRING) var gender: Gender? = null,
        @field:NotBlank(message = "名称不能为空") var name: String = "",
        var keyword: String? = null,
        @field:NotNull(message = "回复类型不能为空") @Enumerated(EnumType.STRING) var type: MessageType? = null,
        var text: String = "",
        var mediaId: String = "",
        var mediaName: String = "",
        var title: String = "",
        var description: String = "",
        var musicUrl: String = "",
        var hqMusicUrl: String = "",
        var pluginCode: String = "",
        var pluginConfig: String = "") : TenantDto() {
    fun getKeyword(): List<String> {
        if (keyword.isNullOrBlank()) return listOf()
        return keyword!!.split(",")
    }
}

internal data class UpdateDto(
        @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @field:NotNull(message = "ID不能为空") var id: Long? = null,
        @field:NotNull(message = "回复事件不能为空") @Enumerated(EnumType.STRING) var event: Event? = null,
        var tag: Int? = null,
        @Enumerated(EnumType.STRING) var gender: Gender? = null,
        var name: String = "",
        var keyword: String? = null,
        @Enumerated(EnumType.STRING) var type: MessageType? = null,
        var text: String = "",
        var mediaId: String = "",
        var mediaName: String = "",
        var title: String = "",
        var description: String = "",
        var musicUrl: String = "",
        var hqMusicUrl: String = "",
        var pluginCode: String = "",
        var pluginConfig: String = "") : TenantDto() {
    fun getKeyword(): List<String> {
        if (keyword.isNullOrBlank()) return listOf()
        return keyword!!.split(",")
    }
}

data class GetDto(
        @Explain("账号ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @Explain("回复ID") @field:NotNull(message = "回复ID不能为空") var id: Long? = null) : TenantDto()

data class DeleteDto(
        @Explain("账号ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @Explain("回复ID") @field:NotNull(message = "回复ID不能为空") var id: Long? = null) : TenantDto()

internal data class RefreshDto(
        @Explain("账号ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null) : TenantDto()

internal data class ListDto(
        var accountId: Long = 0L,
        var name: String? = null,
        var filter: String? = null) : TenantDto()

internal data class ActionListDto(
        @field:NotNull(message = "回复事件不能为空") @Enumerated(EnumType.STRING) var event: Event? = null) : TenantDto()