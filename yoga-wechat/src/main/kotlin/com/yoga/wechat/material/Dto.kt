package com.yoga.wechat.material

import com.yoga.core.annotation.Explain
import com.yoga.user.basic.TenantDto
import org.hibernate.validator.constraints.NotBlank
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull

data class UploadNewsDto(
        @Explain("账户ID") @field:NotNull(message = "账户ID不能为空") var accountId: Long? = null,
        @Explain("素材ID") @field:NotNull(message = "素材ID不能为空") var materialId: Long? = null) : TenantDto()

data class DelArticleDto(
        @Explain("账户ID") @field:NotNull(message = "账户ID不能为空") var accountId: Long? = null,
        @Explain("文章ID") @field:NotNull(message = "文章ID不能为空") var articleId: Long? = null) : TenantDto()

data class AddArticleDto(
        @Explain("账户ID") @field:NotNull(message = "账户ID不能为空") var accountId: Long? = null,
        @Explain("标题") var title: String? = null,
        @Explain("其他") var intro: String? = null) : TenantDto()

data class UpdateArticleDto(
        @Explain("账户ID") @field:NotNull(message = "账户ID不能为空") var accountId: Long? = null,
        @Explain("标题") var title: String? = null,
        @Explain("其他") var intro: String? = null) : TenantDto()

data class RefreshDto(
        @Explain("账号ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @Enumerated(EnumType.STRING) @field:NotNull(message = "素材类型不能为空") var type: MaterialType? = null) : TenantDto()

data class ListDto(
        @Explain("账号ID") var accountId: Long = 0L,
        @field:NotNull(message = "素材类型不能为空") var type: MaterialType? = null,
        var name: String? = null,
        var groupId: Long? = null) : TenantDto()

data class ArticleListDto(
        @Explain("账号ID") var accountId: Long = 0L,
        var name: String? = null,
        var groupId: Long? = null) : TenantDto()

data class MediaDto(
        @Explain("账号ID") var accountId: Long = 0L,
        @field:NotNull(message = "MediaID不能为空") var mediaId: Long? = null) : TenantDto()

data class MediaFile(
        var url: String = "",
        var file: String = "")

data class ListDto2(
        @field:NotNull(message = "账号ID不能为空") @Explain("账号ID") var accountId: Long? = null,
        @field:NotNull(message = "素材类型不能为空") var type: MaterialType? = null,
        var name: String? = null,
        var groupId: Long? = null) : TenantDto()

data class DetailDto(
        @Explain("账号ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @Explain("素材ID") @field:NotNull(message = "素材ID不能为空") var id: Long? = null
) : TenantDto()

data class AddBean(
        @Explain("账号ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @Enumerated(EnumType.STRING) @field:NotNull(message = "素材类型不能为空") var type: MaterialType? = null,
        var groupId: Long = 0,
        var name: String? = null,
        var title: String? = null,
        var introduction: String? = null,
        @field:NotEmpty(message = "素材不能为空") var files: List<MediaFile>? = null)

data class GetOneDto(
        @Explain("菜单ID") @field:NotNull(message = "菜单ID不能为空") var entityId: Long? = null) : TenantDto()

data class GetDto(
        @Explain("素材") @field:NotNull(message = "mediaId不能为空") var mediaId: String? = null) : TenantDto()

data class BatchDeleteDto(
        @Explain("账号ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @Explain("素材IDs") @field:NotNull(message = "素材ID不能为空") var mediaIds: Array<String>? = null) : TenantDto()

data class EditDto(
        @Explain("mediaId") @field:NotNull(message = "mediaID不能为空") var mediaId: String? = null,
        @Explain("素材名称") var name: String? = null) : TenantDto()

data class DeleteDto(
        @Explain("账号ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @Explain("素材ID") @field:NotNull(message = "素材ID不能为空") var id: Long? = null) : TenantDto()

data class PublishDto(
        @Explain("账号ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @Explain("素材ID") @field:NotNull(message = "素材ID不能为空") var id: Long? = null) : TenantDto()

data class UrlDto(
        @Explain("素材ID") @field:NotNull(message = "素材ID不能为空") var mediaId: String? = null) : TenantDto()


data class GroupListDto(
        var name: String? = null) : TenantDto()

data class GroupAddDto(
        @field:NotBlank(message = "分组名称不能为空") var name: String = "",
        var remark: String = "") : TenantDto()

data class GroupUpdateDto(
        @field:NotNull(message = "分组ID不能为空") var id: Long? = null,
        var name: String? = null,
        var remark: String? = null) : TenantDto()

data class GroupDeleteDto(
        @field:NotNull(message = "分组ID不能为空") var id: Long? = null) : TenantDto()

data class GroupSetDto(
        @field:NotNull(message = "分组ID不能为空") var groupId: Long? = null,
        @field:NotEmpty(message = "素材ID不能为空") var mediaIds: Array<String>? = null) : TenantDto()



