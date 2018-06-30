package com.yoga.wechat.menu

import com.yoga.core.controller.BaseWebController
import com.yoga.core.exception.IllegalArgumentException
import com.yoga.tenant.setting.Settable
import com.yoga.user.basic.TenantPage
import com.yoga.wechat.account.AccountService
import com.yoga.wechat.actions.ActionsService
import freemarker.ext.beans.BeansWrapper
import org.apache.commons.collections.map.HashedMap
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@Settable
@Controller
@EnableAutoConfiguration
@RequestMapping("/wechat/menu")
open class MenuWebController @Autowired constructor(
        val accountService: AccountService,
        val menuService: MenuService,
        val actionsService: ActionsService) : BaseWebController() {

    @RequiresAuthentication
    @RequestMapping("")
    open fun defaultMenu(page: TenantPage, dto: ListDto, model: ModelMap): String {
        val accounts = accountService.all(dto.tid);
        if (dto.accountId == 0L && accounts.size > 0) dto.accountId = accounts.get(0).id
        val param = HashedMap()
        param.put("accountId", dto.accountId)
        param.put("isdefault", 1)
        model.put("param", param)
        model.put("accounts", accounts)
        val defaultMenu = if (dto.accountId != 0L) menuService.defaultMenu(dto.tid, dto.accountId) else null
        model.put("menu", defaultMenu)
        return "/account/menu"
    }

    @RequiresAuthentication
    @RequestMapping("/condition")
    open fun conditions(page: TenantPage, dto: ListDto, model: ModelMap): String {
        val accounts = accountService.all(dto.tid);
        if (dto.accountId == 0L && accounts.size > 0) dto.accountId = accounts.get(0).id
        val param = HashedMap()
        param.put("accountId", dto.accountId)
        model.put("param", param)
        model.put("accounts", accounts)
        if (dto.accountId != 0L) {
            val conditionalMenus = menuService.conditionalMenus(dto.tid, dto.accountId, false)
            model.put("conditionalMenus", conditionalMenus)
        }
        model.put("enums", BeansWrapper.getDefaultInstance().getEnumModels());
        return "/account/condition"
    }

    @RequiresAuthentication
    @RequestMapping("/edit")
    open fun conditionMenu(page: TenantPage, @Valid dto: EditMenuDto, bindingResult: BindingResult, model: ModelMap): String {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val menu = menuService.getMenu(dto.tid, dto.menuId!!)
        model.put("menu", menu)
        val param = HashedMap()
        param.put("isdefault", 0)
        model.put("param", param)
        val cond = HashedMap()
        cond.put("accountId", dto.accountId)
        cond.put("menuId", dto.menuId)
        model.put("cond", cond)
        return "/account/menu"
    }

    @RequiresAuthentication
    @RequestMapping("/action/config")
    open fun configAction(@Valid dto: ActionConfigDto, bindingResult: BindingResult, model: ModelMap, request: HttpServletRequest, response: HttpServletResponse): String {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val action = actionsService.action(dto.code!!)
        if (action == null) throw IllegalArgumentException("不存在该插件")
        return action.action.showConfig(dto.tid, request, response, model, dto.config)
    }

    @RequiresAuthentication
    @RequestMapping("/param")
    open fun trasParam(): String {
        return "/account/menu_entity"
    }

}
