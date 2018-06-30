package com.yoga.wechat.material

import com.yoga.core.annotation.Explain
import com.yoga.core.data.CommonResult
import com.yoga.core.exception.BusinessException
import com.yoga.core.exception.IllegalArgumentException
import com.yoga.core.utils.DateUtil
import com.yoga.core.utils.MapConverter
import com.yoga.user.basic.TenantDto
import com.yoga.user.basic.TenantPage
import com.yoga.user.model.LoginUser
import com.yoga.wechat.base.WxBaseApiController
import com.yoga.wechat.menu.ArticleDetailDto
import com.yoga.wechat.users.tags.TagsService
import org.apache.commons.collections.map.HashedMap
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartHttpServletRequest
import java.util.*
import javax.servlet.http.Part
import javax.validation.Valid

@Explain("WX素材管理", module = "wx_material")
@RestController(value = "wechatMediaApiController")
@RequestMapping("/api/wechat/material")
open class MaterialApiController @Autowired constructor(
        val materialService: MaterialService,
        val tagsService: TagsService,
        val materialGroupService: MaterialGroupService) : WxBaseApiController() {

    @Explain("提交图文到微信")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/news/upload")
    open fun uploadNews(@Valid dto: UploadNewsDto, bindingResult: BindingResult): CommonResult {
        materialService.uploadNews(dto.tid, dto.accountId!!, dto.materialId!!)
        return CommonResult()
    }

    @Explain("更新单个图文")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/article/update")
    open fun updateArticle(request: MultipartHttpServletRequest, @Valid dto: UpdateArticleDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        var parts: Collection<Part>? = null
        parts = request.parts
        val fields = HashMap<String, String>()
        for (part in parts!!) {
            val name = part.name
            val bytes = ByteArray(part.size.toInt())
            part.inputStream.read(bytes)
            var value: String? = fields[name]
            if (value == null)
                value = String(bytes)
            else
                value += "," + String(bytes)
            fields.put(name, value)
        }
        val title = fields["title"]
        if (title.isNullOrBlank()) throw BusinessException("请输入文章标题")
        val article = materialService.updateArticle(dto.tid, dto.accountId!!, dto.title, dto.intro, fields)
        return CommonResult(article)
    }

    @Explain("删除单个图文")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/article/del")
    open fun delArticle(@Valid dto: DelArticleDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        materialService.delArticle(dto.tid, dto.articleId!!)
        return CommonResult();
    }

    @Explain("新增单个图文")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/article/add")
    open fun addArticle(request: MultipartHttpServletRequest, @Valid dto: AddArticleDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        var parts: Collection<Part>? = null
        parts = request.parts
        val fields = HashMap<String, String>()
        for (part in parts!!) {
            val name = part.name
            val bytes = ByteArray(part.size.toInt())
            part.inputStream.read(bytes)
            var value: String? = fields[name]
            if (value == null)
                value = String(bytes)
            else
                value += "," + String(bytes)
            fields.put(name, value)
        }
        val title = fields["title"]
        if (title.isNullOrBlank()) throw BusinessException("请输入文章标题")

        val article = materialService.addArticle(dto.tid, dto.accountId!!, title, dto.intro, fields)
        return CommonResult(article)
    }

    @Explain("编辑图文")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/article/edit")
    open fun newsEdit(@Valid dto: ArticleDetailDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val map = HashedMap()
        map.put("accountId", dto.accountId)
        val article = materialService.getArticleDetail(dto.tid, dto.accountId!!, dto.articleId!!)
        map.put("article", article)
        map.put("article", MapConverter(MapConverter.Converter<MaterialArticle> { item, map ->
            with(item) {
                map.set("articleId", id)
                        .set("materialId", materialId)
                        .set("title", title)
                        .set("author", author)
                        .set("thumbUrl", thumbUrl)
                        .set("thumbMediaId", thumbMediaId)
                        .set("digest", digest)
                        .set("content", content)
                        .set("sourceUrl", sourceUrl)
                        .set("showConverImage", showConverImage)
                        .set("clickUrl", clickUrl)
            }
        }).build(article))
        return CommonResult(map)
    }

    @Explain("从公众号刷新")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/refresh")
    open fun refresh(user: LoginUser, @Valid dto: RefreshDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        materialService.sync(dto.tid, dto.accountId!!, user.nickname, user.id, dto.type!!)
        return CommonResult()
    }

    //将废弃
    @Explain("菜单绑定的素材")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/getone")
    open fun getOne(@Valid dto: GetOneDto, bindingResult: BindingResult): CommonResult {
        return CommonResult(materialService.getMaterial(dto.tid, dto.entityId!!))
    }

    @Explain("菜单绑定的素材")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/get")
    open fun getMaterialByMediaId(@Valid dto: GetDto, bindingResult: BindingResult): CommonResult {
        return CommonResult(materialService.getMaterial(dto.tid, dto.mediaId!!))
    }


    @Explain("素材列表")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/list")
    open fun list(user: LoginUser, page: TenantPage, @Valid dto: ListDto2, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val images = materialService.list(dto.tid, dto.accountId!!, dto.type!!, dto.name, dto.groupId, null, page.pageIndex, page.pageSize)
        return CommonResult(MapConverter(MapConverter.Converter<MaterialAndEx> { item, map ->
            with(item) {
                map.set("id", id)
                        .set("name", name ?: "")
                        .set("url", url)
                        .set("mediaId", mediaId)
                        .set("title", title)
                        .set("intro", intro)
                        .set("groupId", groupId)
                        .set("groupName", groupName)
                        .set("remark", remark)
                        .set("articles", articles)
                        .set("updateTime", DateUtil.formatDateShort(updateTime))
            }
        }).build(images), images.page)
    }

    @Explain("素材详情")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/detail")
    open fun getDetail(@Valid dto: DetailDto, bindingResult: BindingResult): CommonResult {
        val material = materialService.getArticleDetail(dto.tid, dto.accountId!!, dto.id!!)
        return CommonResult(material)
    }

    @Explain("编辑素材名称")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/edit")
    open fun edit(@Valid dto: EditDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        materialService.updateName(dto.tid, dto.mediaId!!, dto.name)
        return CommonResult()
    }

    @Explain("添加素材")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/add")
    open fun add(user: LoginUser, @Valid dto: TenantDto, @Valid @RequestBody bean: AddBean, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val mediaIdList = materialService.add(dto.tid, bean.accountId!!, bean.groupId, bean.type!!, bean.name, bean.title, bean.introduction, bean.files!!)
        return CommonResult(mediaIdList)
    }

    @Explain("批量删除素材")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/batchDelete")
    open fun batchDelete(@Valid dto: BatchDeleteDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        materialService.batchDelete(dto.tid, dto.accountId!!, dto.mediaIds!!)
        return CommonResult()
    }

    @Explain("删除素材")
    @RequiresPermissions("wx_material.update")
    @RequestMapping("/delete")
    open fun delete(user: LoginUser, @Valid dto: DeleteDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        materialService.delete(dto.tid, dto.accountId!!, dto.id!!)
        return CommonResult()
    }

    @Explain("设置分组")
    @RequiresPermissions("wx_users.update")
    @RequestMapping("/group/set")
    open fun addUser(@Valid dto: GroupSetDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        materialService.setGroup(dto.tid, dto.mediaIds!!, dto.groupId!!)
        return CommonResult()
    }

    @Explain("创建分组")
    @RequiresPermissions("wx_material_group.update")
    @RequestMapping("/group/add")
    open fun add(@Valid dto: GroupAddDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        materialGroupService.add(dto.tid, dto.name, dto.remark)
        return CommonResult()
    }

    @Explain("修改分组")
    @RequiresPermissions("wx_material_group.update")
    @RequestMapping("/group/update")
    open fun update(@Valid dto: GroupUpdateDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        materialGroupService.update(dto.tid, dto.id!!, dto.name, dto.remark)
        return CommonResult()
    }

    @Explain("删除分组")
    @RequiresPermissions("wx_material_group.update")
    @RequestMapping("/group/delete")
    open fun delete(@Valid dto: GroupDeleteDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        materialGroupService.delete(dto.tid, dto.id!!)
        return CommonResult()
    }
}
