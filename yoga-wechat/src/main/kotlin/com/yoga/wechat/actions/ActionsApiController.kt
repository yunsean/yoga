package com.yoga.wechat.actions

import com.yoga.core.annotation.Explain
import com.yoga.core.data.CommonResult
import com.yoga.core.exception.IllegalArgumentException
import com.yoga.user.model.LoginUser
import com.yoga.wechat.base.WxBaseApiController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Explain(exclude = true)
@RestController(value = "wechatActionsApiController")
@RequestMapping("/api/wechat/actions")
internal open class ActionsApiController @Autowired constructor(
        val actionsService: ActionsService) : WxBaseApiController() {

    @RequestMapping("/items")
    open fun actionItems(user: LoginUser, @Valid dto: ListDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        if (dto.event == null) return CommonResult(actionsService.actions())
        val actions = actionsService.actions(dto.event!!)
        return CommonResult(actions)
    }
}