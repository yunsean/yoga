package com.yoga.wechat.menu

import com.yoga.core.annotation.Explain
import com.yoga.user.basic.TenantDto
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull

data class NewsPreviewDto(
        @Explain("账户ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @Explain("文章ID") @field:NotNull(message = "文章ID不能为空") var articleId: Long? = null,
        @Explain("更新时间") var updateTime: String? = null) : TenantDto()

data class NewsDetailDto(
        @Explain("账户ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @Explain("mediaID") @field:NotNull(message = "mediaID不能为空") var mediaId: String? = null) : TenantDto()

data class ArticleDetailDto(
        @Explain("账户ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @Explain("articleId") @field:NotNull(message = "ArticleId不能为空") var articleId: Long? = null) : TenantDto()

data class RefreshDto(
        @Explain("账号ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null) : TenantDto()

data class PublishDto(
        @Explain("账号ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null) : TenantDto()

data class ListDto(
        @Explain("账号ID") var accountId: Long = 0L) : TenantDto()

data class EditMenuDto(
        @Explain("账号ID") @field:NotNull(message = "账号ID不能为空") var accountId: Long? = null,
        @Explain("菜单ID") @field:NotNull(message = "菜单ID不能为空") var menuId: Long? = null) : TenantDto()

data class GetDto(
        @Explain("菜单ID") @field:NotNull(message = "菜单ID不能为空") var id: Long? = null) : TenantDto()

data class AddDto(
        @Explain("账户ID") @field:NotNull(message = "账户ID不能为空") var accountId: Long? = null,
        var isDefault: Boolean = false,
        @field:NotEmpty(message = "菜单名称不能为空") var name: String? = null,
        var remark: String? = null,
        var tagId: String? = null,
        @field:Enumerated(EnumType.STRING) var gender: RuleGender? = null,
        var country: String? = null,
        var province: String? = null,
        var city: String? = null,
        @field:Enumerated(EnumType.STRING) var clientOS: RuleOs? = null,
        @field:Enumerated(EnumType.STRING) var language: RuleLanguage? = null) : TenantDto()

data class UpdateDto(
        @Explain("菜单ID") @field:NotNull(message = "菜单ID不能为空") var id: Long? = null,
        var name: String? = null,
        var remark: String? = null,
        var tagId: String? = null,
        @field:Enumerated(EnumType.STRING) var gender: RuleGender? = null,
        var country: String? = null,
        var province: String? = null,
        var city: String? = null,
        @field:Enumerated(EnumType.STRING) var clientOS: RuleOs? = null,
        @field:Enumerated(EnumType.STRING) var language: RuleLanguage? = null) : TenantDto()

data class DeleteDto(
        @Explain("菜单ID") @field:NotNull(message = "菜单ID不能为空") var id: Long? = null) : TenantDto()

data class GetEntityDto(
        @Explain("菜单项ID") @field:NotNull(message = "菜单项ID不能为空") var id: Long? = null) : TenantDto()

data class AddEntityDto(
        @Explain("菜单ID") @field:NotNull(message = "菜单ID不能为空") var menuId: Long? = null,
        @Explain("菜单类型") @field:NotNull(message = "菜单项类型不能为空") var type: MenuType? = MenuType.none,
        @Explain("菜单项排序值") var sort: Int = 0,
        @Explain("显示名称") @field:NotEmpty(message = "菜单项名称不能为空") var name: String? = null,
        @Explain("网页跳转URL") var url: String? = null,
        @Explain("媒体对象ID") var mediaId: String? = null,
        @Explain("关键字") var key: String? = null,
        @Explain("小程序ID") var appId: String? = null,
        @Explain("小程序页面路径") var pagePath: String? = null,
        @Explain("父菜单ID") var parentId: Long = 0,
        @Explain("插件配置") var rawConfig: String? = null,
        @Explain("插件名称") var displayConfig: String? = null,
        @Explain("插件CODE") var pluginCode: String? = null,
        @Explain("子菜单") var children: List<AddEntityDto>? = null)

data class UpdateEntityDto(
        @Explain("菜单项ID") @field:NotNull(message = "菜单项ID不能为空") var id: Long? = null,
        var type: MenuType? = null,
        @Explain("菜单项排序值") var sort: Int = 0,
        var name: String? = null,
        var url: String? = null,
        var mediaId: String? = null,
        var key: String? = null,
        var appId: String? = null,
        var pagePath: String? = null) : TenantDto()


data class ToEntityEditDto(
        @Explain("账户ID") var accountId: Long? = null,
        @Explain("菜单ID") var menuId: Long? = null,
        @Explain("是否默认菜单") @field:NotNull(message = "需要指定是否为默认菜单,默认 0 ") var isdefault: Boolean = false) : TenantDto()

data class DeleteEntityDto(
        @Explain("菜单项ID") @field:NotNull(message = "菜单项ID不能为空") var id: Long? = null) : TenantDto()

data class ActionConfigDto(
        @Explain("配置插件CODE") @field:NotNull(message = "插件CODE不能为空") var code: String? = null,
        @Explain("原始配置项") var config: String? = null) : TenantDto()

