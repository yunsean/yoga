package com.yoga.wechat.weiweb

import com.yoga.core.annotation.Explain
import com.yoga.core.controller.BaseApiController
import com.yoga.core.data.CommonResult
import com.yoga.core.exception.IllegalArgumentException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@Explain("WX网页开发", exclude = true)
@RestController
@RequestMapping("/api/wechat/jssdk")
open class WeiXinJsSdkApiController : BaseApiController() {
    @Autowired
    private val weiXingJsSdkService: WeiXinJsSdkService? = null

    @Explain("JSSDK初始化")
    @RequestMapping("/config")
    open fun getConfig(@Valid dto: GetJSConfigDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val result = weiXingJsSdkService?.getConfig(dto.tid, dto.accountId!!, dto.url!!)
        return CommonResult(result)
    }

    @RequestMapping("/wk_config")
    open fun getConfig(@Valid dto: WKGetJSConfigDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val result = weiXingJsSdkService?.getConfig(dto.sTid!!, dto.accountId!!, dto.url!!)
        return CommonResult(result)
    }
}