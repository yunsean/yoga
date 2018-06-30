package com.yoga.wechat.reply

import com.yoga.core.controller.BaseWebController
import com.yoga.core.data.PageList
import com.yoga.tenant.setting.Settable
import com.yoga.user.basic.TenantPage
import com.yoga.wechat.account.AccountService
import com.yoga.wechat.material.MaterialGroupService
import com.yoga.wechat.users.tags.TagsService
import org.apache.commons.collections.map.HashedMap
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping

@Settable
@Controller
@EnableAutoConfiguration
@RequestMapping("/wechat/reply")
internal open class ReplyWebController @Autowired constructor(
        val accountService: AccountService,
        val tagsService: TagsService,
        val replyService: ReplyService,
        val materialGroupService: MaterialGroupService) : BaseWebController() {

    @RequiresAuthentication
    @RequestMapping("")
    open fun list(page: TenantPage, dto: ListDto, model: ModelMap): String {
        val accounts = accountService.all(dto.tid);
        if (dto.accountId == 0L && accounts.size > 0) dto.accountId = accounts.get(0).id
        val param = HashedMap()
        param.put("accountId", dto.accountId)
        param.put("name", dto.name)
        param.put("filter", dto.filter)
        model.put("param", param)
        val replies = if (dto.accountId != 0L) replyService.list(dto.tid, dto.accountId, dto.name, null, page.pageIndex, page.pageSize) else PageList()
        val tags = if (dto.accountId != 0L) tagsService.map(dto.tid, dto.accountId) else mapOf()
        val groups = materialGroupService.list(dto.tid, null, 0, 1000)
        model.put("groups", groups)
        model.put("tags", tags)
        model.put("accounts", accounts)
        model.put("replies", replies)
        model.put("page", replies.page)
        model.put("actions", Event.values())
        return "/account/reply"
    }
}
