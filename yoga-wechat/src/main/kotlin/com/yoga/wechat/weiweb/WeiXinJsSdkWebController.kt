package com.yoga.wechat.weiweb

import com.yoga.core.annotation.Explain
import com.yoga.core.controller.BaseApiController
import com.yoga.core.exception.IllegalArgumentException
import com.yoga.core.property.PropertiesService
import com.yoga.wechat.oauth2.OAuth2Service
import com.yoga.wechat.weixin.WeixinServiceFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import java.util.logging.Logger
import javax.validation.Valid


@Explain("WX网页开发", exclude = true)
@Controller
@RequestMapping("/wechat/jssdk")
open class WeiXinJsSdkWebController @Autowired constructor(
        val weixinServiceFactory: WeixinServiceFactory,
        val oAuth2Service: OAuth2Service,
        val propertiesService: PropertiesService) : BaseApiController() {

    @Explain("JSSDK初始化")
    @RequestMapping("/redirect")
    open fun getConfig(@Valid dto: RedirectDto, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        Logger.getLogger("dylan").warning(dto.url)
        val url = oAuth2Service.getOpenIdUrl(dto.accountId!!, dto.url)
        return "redirect:${url}"
    }
}