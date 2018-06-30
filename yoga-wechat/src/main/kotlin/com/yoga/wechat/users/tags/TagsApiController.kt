package com.yoga.wechat.users.tags

import com.yoga.core.annotation.Explain
import com.yoga.core.data.CommonResult
import com.yoga.core.exception.IllegalArgumentException
import com.yoga.user.model.LoginUser
import com.yoga.wechat.base.WxBaseApiController
import com.yoga.wechat.users.UserService
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Explain("WX标签管理", module = "wx_tags")
@RestController(value = "wechatTagsApiController")
@RequestMapping("/api/wechat/users/tags")
open class TagsApiController @Autowired constructor(
        val tagsService: TagsService,
        val userService: UserService) : WxBaseApiController() {

    @Explain("创建用户标签")
    @RequiresPermissions("wx_tags.update")
    @RequestMapping("/add")
    open fun add(user: LoginUser, @Valid dto: AddDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        tagsService.add(dto.tid, dto.accountId!!, dto.name)
        return CommonResult()
    }
    @Explain("修改标签信息")
    @RequiresPermissions("wx_tags.update")
    @RequestMapping("/update")
    open fun update(user: LoginUser, @Valid dto: UpdateDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        tagsService.update(dto.tid, dto.accountId!!, dto.id!!, dto.name)
        return CommonResult()
    }
    @Explain("删除标签")
    @RequiresPermissions("wx_tags.update")
    @RequestMapping("/delete")
    open fun delete(user: LoginUser, @Valid dto: DeleteDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        tagsService.delete(dto.tid, dto.accountId!!, dto.id!!)
        return CommonResult()
    }

    @Explain("删除用户")
    @RequiresPermissions("wx_users.update")
    @RequestMapping("/user/remove")
    open fun removeUser(user: LoginUser, @Valid dto: TagUsersDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        userService.removeTag(dto.tid, dto.accountId!!, dto.tagId!!, dto.openIds!!)
        return CommonResult()
    }
    @Explain("添加用户")
    @RequiresPermissions("wx_users.update")
    @RequestMapping("/user/add")
    open fun addUser(user: LoginUser, @Valid dto: TagUsersDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        userService.addTag(dto.tid, dto.accountId!!, dto.tagId!!, dto.openIds!!)
        return CommonResult()
    }
}
