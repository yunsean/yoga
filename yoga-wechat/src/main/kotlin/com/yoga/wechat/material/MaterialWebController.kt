package com.yoga.wechat.material

import com.yoga.core.controller.BaseWebController
import com.yoga.core.data.PageList
import com.yoga.core.exception.IllegalArgumentException
import com.yoga.core.property.PropertiesService
import com.yoga.user.basic.TenantPage
import com.yoga.tenant.setting.Settable
import com.yoga.wechat.account.AccountService
import com.yoga.wechat.menu.NewsDetailDto
import com.yoga.wechat.menu.NewsPreviewDto
import com.yoga.wechat.users.tags.TagsService
import org.apache.commons.collections.map.HashedMap
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.validation.Valid

@Settable
@Controller(value = "wechatMediaWebController")
@EnableAutoConfiguration
@RequestMapping("/wechat/material")
open class MaterialWebController @Autowired constructor(
        val accountService: AccountService,
        val materialService: MaterialService,
        val tagsService: TagsService,
        val materialGroupService: MaterialGroupService,
        val propertiesService: PropertiesService) : BaseWebController() {

    @RequiresAuthentication
    @RequestMapping("")
    open fun mediaList(model: ModelMap, page: TenantPage, @Valid dto: ListDto, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val accounts = accountService.all(dto.tid);
        if (dto.accountId == 0L && accounts.size > 0) dto.accountId = accounts.get(0).id
        val param = HashedMap()
        param.put("accountId", dto.accountId)
        param.put("name", dto.name)
        param.put("type", dto.type)
        param.put("groupId", dto.groupId)
        model.put("param", param)
        model.put("accounts", accounts)
        val images = if (dto.accountId != 0L) materialService.list(dto.tid, dto.accountId, dto.type!!, dto.name, dto.groupId, null, page.pageIndex, page.pageSize) else PageList()
        model.put("medias", images)
        model.put("page", images.getPage())
        model.put("groups", materialGroupService.list2(dto.tid, dto.accountId, dto.type!!))
        model.put("uploadPath", propertiesService.fineUploadUrl)
        return when (dto.type) {
            MaterialType.image -> "/media/materials_image"
            MaterialType.news -> "/media/materials_news"
            MaterialType.video -> "/media/materials_video"
            MaterialType.voice -> "/media/materials_voice"
            else -> "/media/materials"
        }
    }

    @RequiresAuthentication
    @RequestMapping("/document")
    open fun articleList(model: ModelMap, page: TenantPage, @Valid dto: ArticleListDto, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val accounts = accountService.all(dto.tid);
        if (dto.accountId == 0L && accounts.size > 0) dto.accountId = accounts.get(0).id
        val param = HashedMap()
        param.put("accountId", dto.accountId)
        param.put("name", dto.name)
        param.put("type", MaterialType.news)
        param.put("groupId", dto.groupId)
        model.put("param", param)
        model.put("accounts", accounts)
        val news = if (dto.accountId != 0L) materialService.list(dto.tid, dto.accountId, MaterialType.news, dto.name, dto.groupId, null, page.pageIndex, page.pageSize) else PageList()
        model.put("medias", news)
        model.put("page", news.getPage())
        model.put("groups", materialGroupService.list2(dto.tid, dto.accountId, MaterialType.news))
        model.put("uploadPath", propertiesService.fineUploadUrl)
        return "/media/materials_news"
    }

    @RequiresAuthentication
    @RequestMapping("/group")
    open fun groupList(model: ModelMap, page: TenantPage, @Valid dto: GroupListDto, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val param = HashedMap()
        param.put("name", dto.name)
        model.put("param", param)
        val groups = materialGroupService.list(dto.tid, dto.name, page.pageIndex, page.pageSize)
        model.put("groups", groups)
        model.put("page", groups.getPage())
        return "/media/groups"
    }

    @RequiresAuthentication
    @RequestMapping("/news/preview")
    open fun newsPreview(model: ModelMap, @Valid dto: NewsPreviewDto, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val article = materialService.getArticleDetail(dto.tid, dto.accountId!!, dto.articleId!!)
        model.put("article", article)
        val param = HashedMap()
        param.put("updateTime", dto.updateTime)
        model.put("param", param)
        return "/media/news_preview"
    }

    @RequiresAuthentication
    @RequestMapping("/news/edit")
    open fun newsDetail(model: ModelMap, @Valid dto: NewsDetailDto, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val param = HashedMap()
        param.put("accountId", dto.accountId)
        param.put("mediaId", dto.mediaId)
        param.put("type", MaterialType.image)
        val news = if (dto.accountId != 0L) materialService.list(dto.tid, dto.accountId!!, MaterialType.news, null, null, dto.mediaId, 0, 1) else PageList()
        model.put("medias", news)
        var article: MaterialArticle? = if (news.isEmpty() || news[0].articles!!.isEmpty()) throw IllegalArgumentException("文章为空") else news.get(0).articles?.get(0)
        model.put("article", article)
        param.put("materialId", article?.materialId)
        model.put("param", param)
        model.put("uploadPath", propertiesService.fineUploadUrl)
        val tags = if (dto.accountId != 0L) tagsService.map(dto.tid, dto.accountId!!) else mapOf()
        val groups = materialGroupService.list(dto.tid, null, 0, 1000)
        model.put("groups", groups)
        model.put("tags", tags)
        return "/media/news_edit"
    }

    @RequiresAuthentication
    @RequestMapping("/news/add")
    open fun newsAdd(model: ModelMap, @RequestParam accountId: Long): String {
        val param = HashedMap()
        param.put("accountId", accountId)
        param.put("type", MaterialType.image)
        model.put("uploadPath", propertiesService.fineUploadUrl)
        model.put("param", param)
        return "/media/news_edit"
    }
}
