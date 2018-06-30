package com.yoga.wechat.users.tags

import com.yoga.core.controller.BaseWebController
import com.yoga.core.data.PageList
import com.yoga.core.exception.IllegalArgumentException
import com.yoga.user.basic.TenantPage
import com.yoga.tenant.setting.Settable
import com.yoga.wechat.account.AccountService
import com.yoga.wechat.users.UserService
import org.apache.commons.collections.map.HashedMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@Settable
@Controller(value = "wechatTagsWebController")
@EnableAutoConfiguration
@RequestMapping("/wechat/users/tags")
open class TagsWebController @Autowired constructor(
        val accountService: AccountService,
        val tagsService: TagsService,
        val userService: UserService) : BaseWebController() {

    @RequestMapping("")
    open fun tagsList(model: ModelMap, dto: ListDto): String {
        val accounts = accountService.all(dto.tid);
        if (dto.accountId == 0L && accounts.size > 0) dto.accountId = accounts.get(0).id
        val param = HashedMap()
        param.put("accountId", dto.accountId)
        param.put("name", dto.name)
        model.put("param", param)
        model.put("accounts", accounts)
        var tags = if (dto.accountId != 0L) tagsService.list(dto.tid, dto.accountId) else PageList()
        if (!dto.name.isNullOrBlank()) {
            tags = tags.filter { it.name.contains(dto.name) }
        }
        model.put("tags", tags)
        return "/users/tags"
    }

    @RequestMapping("/users")
    open fun tagUsers(page: TenantPage, model: ModelMap, @Valid dto: UsersDto, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val accounts = accountService.all(dto.tid);
        if (dto.accountId == 0L && accounts.size > 0) dto.accountId = accounts.get(0).id
        val param = HashedMap()
        param.put("accountId", dto.accountId)
        param.put("tagId", dto.tagId)
        param.put("nickname", dto.nickname)
        param.put("gender", dto.gender)
        model.put("param", param)
        val users = userService.list(dto.tid, dto.accountId!!, dto.nickname, dto.tagId!!, dto.gender, page.pageIndex, page.pageSize)
        model.put("users", users)
        model.put("page", users.page)
        return "/users/tagUsers"
    }
}
