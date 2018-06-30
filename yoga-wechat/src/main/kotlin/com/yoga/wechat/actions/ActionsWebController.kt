package com.yoga.wechat.actions

import com.yoga.core.controller.BaseWebController
import com.yoga.core.exception.IllegalArgumentException
import com.yoga.tenant.setting.Settable
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
@RequestMapping("/wechat/actions")
open class ActionsWebController @Autowired constructor(
        val actionsService: ActionsService) : BaseWebController() {
    @RequiresAuthentication
    @RequestMapping("/config")
    open fun configAction(@Valid dto: ConfigDto, bindingResult: BindingResult, model: ModelMap, request: HttpServletRequest, response: HttpServletResponse): String {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val action = actionsService.action(dto.code!!)
        if (action == null) throw IllegalArgumentException("不存在该插件")
        return action.action.showConfig(dto.tid, request, response, model, dto.config)
    }
}