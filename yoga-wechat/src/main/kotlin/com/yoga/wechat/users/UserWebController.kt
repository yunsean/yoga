package com.yoga.wechat.users

import com.yoga.core.controller.BaseWebController
import com.yoga.core.data.PageList
import com.yoga.user.basic.TenantPage
import com.yoga.tenant.setting.Settable
import com.yoga.wechat.account.AccountService
import com.yoga.wechat.users.tags.TagsService
import org.apache.commons.collections.map.HashedMap
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping

@Settable
@Controller(value = "wechatUserWebController")
@EnableAutoConfiguration
@RequestMapping("/wechat/users")
open class UserWebController @Autowired constructor(
        val accountService: AccountService,
        val userService: UserService,
        val tagsService: TagsService) : BaseWebController() {

    @RequiresAuthentication
    @RequestMapping("")
    open fun userList(page: TenantPage, dto: ListDto, model: ModelMap): String {
        val accounts = accountService.all(dto.tid);
        if (dto.accountId == 0L && accounts.size > 0) dto.accountId = accounts.get(0).id
        val param = HashedMap()
        param.put("nickname", dto.nickname)
        param.put("accountId", dto.accountId)
        param.put("gender", dto.gender)
        model.put("param", param)
        model.put("accounts", accounts)
        val users = if (dto.accountId != 0L) userService.listEx(dto.tid, dto.accountId, dto.nickname, null, dto.gender, page.pageIndex, page.pageSize) else PageList()
        model.put("users", users)
        model.put("page", users.getPage())
        var tags = if (dto.accountId != 0L) tagsService.list(dto.tid, dto.accountId) else listOf()
        model.put("tags", tags)
        return "/users/users"
    }
}
