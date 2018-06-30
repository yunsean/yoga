package com.yoga.wechat.oauth2

import com.yoga.core.annotation.Explain
import com.yoga.core.controller.BaseEndController
import com.yoga.core.data.CommonResult
import com.yoga.core.data.ResultConstants
import com.yoga.core.utils.StrUtil
import com.yoga.tenant.tenant.service.TenantService
import com.yoga.user.dept.service.DepartmentService
import com.yoga.user.model.LoginUser
import com.yoga.user.shiro.SuperAdminUser
import com.yoga.user.shiro.TenantToken
import com.yoga.wechat.users.UserService
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import java.net.URLEncoder
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@Explain(exclude = true)
@Controller("wechatOAuth2EndController")
@RequestMapping("/wechat/oauth2")
open class OAuth2EndController @Autowired constructor(
        val tenantService: TenantService,
        val departmentService: DepartmentService,
        val superAdminUser: SuperAdminUser,
        val userService: UserService): BaseEndController() {

    @RequestMapping("/nowechat")
    open fun noWechat(): String {
        return "/wechat/web/nowechat";
    }
    open fun returnLogin(model: ModelMap, tenantId: Long, accountId: Long, param: Map<String, Any>): String {
        model.put("param", param)
        val img = HashMap<String, String>()
        val setting = tenantService.getSetting(tenantId)
        img.put("pickLogin", setting.loginLogoUrl)
        img.put("topImage", setting.topImageUrl)
        img.put("loginBg", setting.loginBackUrl)
        model.put("img", img)
        val deptId = departmentService.defaultLogonDept(tenantId)
        val allowLogon = deptId != null && deptId != 0L
        model.put("allowLogon", allowLogon)
        model.put("tenantId", tenantId)
        model.put("accountId", accountId)
        return "/wechat/web/bind"
    }

    @RequestMapping(value = "/toBind")
    fun toBind(request: HttpServletRequest, model: ModelMap, dto: BindDto): String {
        var to = try { URLEncoder.encode(dto.uri, "UTF-8") } catch (_: Exception) {dto.uri}
        if (to.isNullOrBlank()) to = ""
        val url = "/wechat/oauth2/bind?tid=${dto.tid}&aid=${dto.aid}&openId=${dto.openId}&uri=${to}"
        model.put("url", url)
        return "/wechat/web/willbind"
    }

    @RequestMapping(value = "/bind")
    fun bind(request: HttpServletRequest, model: ModelMap, dto: BindDto): String {
        return returnLogin(model, dto.tid, dto.aid, dto.wrapAsMap())
    }

    @RequestMapping(value = "/doBind")
    fun doBind(model: ModelMap, @Valid dto: DoBindDto, bindingResult: BindingResult): String {
        val result = bind(dto, bindingResult)
        if (result.getCode() == 0) {
            val user = LoginUser()
            userService.bind(dto.tid, user.id, dto.aid, dto.openId!!)
            model.put("username", if (user.nickname.isNullOrBlank()) user.username else user.nickname)
            return "/wechat/web/bindok"
        } else {
            model.put("reason", result.getMessage())
            return returnLogin(model, dto.tid, dto.aid, dto.wrapAsMap())
        }
    }

    private fun bind(dto: DoBindDto, bindingResult: BindingResult): CommonResult {
        if (!dto.first && bindingResult.hasErrors()) {
            return CommonResult(ResultConstants.ERROR_ILLEGALPARAM, bindingResult.fieldError.defaultMessage)
        }
        if (StrUtil.hasBlank(dto.username, dto.password)) {
            return CommonResult(ResultConstants.ERROR_ILLEGALPARAM, "")
        }
        if (superAdminUser.isAdmin(dto.username)) {
            return CommonResult(ResultConstants.ERROR_FORBIDDEN, "您没有前台登录的权限！")
        }
        val token = TenantToken(dto.tid, dto.username, dto.password)
        val subject = SecurityUtils.getSubject()
        try {
            subject.login(token)
        } catch (e: AuthenticationException) {
            return CommonResult(ResultConstants.ERROR_FORBIDDEN, "无效的用户名或者密码！")
        }
        return if (!subject.isPermitted("pri_client.login")) CommonResult(ResultConstants.ERROR_FORBIDDEN, "您没有前台登录的权限！")
        else CommonResult()
    }

    companion object {
        const val Url_NotInWechat = "/wechat/oauth2/nowechat"
        const val Url_NotBind = "/wechat/oauth2/toBind"
    }
}