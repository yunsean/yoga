package com.yoga.wechat.account

import com.yoga.core.controller.BaseWebController
import com.yoga.core.property.PropertiesService
import com.yoga.tenant.setting.Settable
import com.yoga.user.basic.TenantPage
import org.apache.commons.collections.map.HashedMap
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@EnableAutoConfiguration
@RequestMapping("/wechat/account")
@Settable(module = AccountService.Module_Name, key = AccountService.MaxAccount_key, name = "微信公众号-最大账号数量", defaultValue = "1", type = Int::class, systemOnly = true)
internal open class AccountWebController @Autowired constructor(
        val accountService: AccountService,
        val propertiesService: PropertiesService) : BaseWebController() {

    @RequiresAuthentication
    @RequestMapping("")
    open fun list(page: TenantPage, dto: ListDto, model: ModelMap): String {
        val param = HashedMap()
        param.put("name", dto.name)
        param.put("filter", dto.filter)
        model.put("param", param)
        model.put("webIp", accountService.getWebIP())
        model.put("tenantId", dto.tid)
        model.put("baseUrl", propertiesService.sysBaseurl)
        model.put("maxCount", accountService.maxAccountCount(dto.tid))
        val accounts = accountService.list(dto.tid, dto.name, dto.filter, page.pageIndex, page.pageSize)
        model.put("accounts", accounts)
        model.put("page", accounts.getPage())
        return "/account/account"
    }
}
