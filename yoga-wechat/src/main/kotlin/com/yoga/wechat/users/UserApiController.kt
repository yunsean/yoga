package com.yoga.wechat.users

import com.yoga.core.annotation.Explain
import com.yoga.core.data.CommonResult
import com.yoga.core.exception.IllegalArgumentException
import com.yoga.user.model.LoginUser
import com.yoga.wechat.base.WxBaseApiController
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Explain("WX用户管理", module = "wx_users")
@RestController(value = "wechatUserApiController")
@RequestMapping("/api/wechat/users")
open class UserApiController @Autowired constructor(
        val userService: UserService) : WxBaseApiController() {

    @Explain("从公众号刷新")
    @RequiresPermissions("wx_users.update")
    @RequestMapping("/refresh")
    open fun refresh(user: LoginUser, @Valid dto: RefreshDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        userService.sync(dto.tid, dto.accountId!!, user.nickname, user.id)
        return CommonResult()
    }

    @Explain("修改用户备注")
    @RequiresPermissions("wx_users.update")
    @RequestMapping("/update")
    open fun update(@Valid dto: UpdateDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        userService.update(dto.tid, dto.accountId!!, dto.openId!!, dto.remark)
        return CommonResult()
    }

    @Explain("绑定用户信息")
    @RequiresAuthentication
    @RequestMapping("/bind")
    open fun binding(loginUser: LoginUser, @Valid dto: BindingDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        userService.bind(dto.tid, loginUser.id, dto.accountId!!, dto.openId!!)
        return CommonResult()
    }

    @Explain("用户TAG列表")
    @RequiresPermissions("wx_users.update")
    @RequestMapping("/tags")
    open fun tags(@Valid dto: TagsDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val tags = userService.tags(dto.tid, dto.accountId!!, dto.openId!!)
        return CommonResult(tags)
    }
    @Explain("删除用户标签")
    @RequiresPermissions("wx_users.update")
    @RequestMapping("/untag")
    open fun untag(@Valid dto: UntagDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        userService.removeTag(dto.tid, dto.accountId!!, dto.tagId!!, arrayOf(dto.openId!!))
        return CommonResult()
    }
    @Explain("添加用户标签")
    @RequiresPermissions("wx_users.update")
    @RequestMapping("/tag")
    open fun tag(@Valid dto: TagDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        userService.addTag(dto.tid, dto.accountId!!, dto.tagId!!, arrayOf(dto.openId!!))
        return CommonResult()
    }
}
