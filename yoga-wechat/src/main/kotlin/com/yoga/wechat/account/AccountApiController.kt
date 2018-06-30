package com.yoga.wechat.account

import com.yoga.core.annotation.Explain
import com.yoga.core.data.CommonResult
import com.yoga.core.exception.IllegalArgumentException
import com.yoga.core.property.PropertiesService
import com.yoga.core.utils.MapConverter
import com.yoga.user.basic.TenantPage
import com.yoga.wechat.base.WxBaseApiController
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@Explain("WX账号管理", module = "wx_account")
@RestController
@RequestMapping("/api/wechat/account")
internal open class AccountApiController @Autowired constructor(
        val accountService: AccountService,
        val propertiesService: PropertiesService) : WxBaseApiController() {

    @Explain("新建账号")
    @RequiresPermissions("wx_account.add")
    @RequestMapping("/add")
    open fun addAccount(@Valid dto: AddDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        accountService.add(dto.tid, dto.name, dto.token, dto.number, dto.rawId, dto.appId, dto.appSecret, dto.aesKey, dto.remark)
        return CommonResult()
    }

    @Explain("删除现有账号")
    @RequiresPermissions("wx_account.del")
    @RequestMapping("/delete")
    open fun delChannel(@Valid dto: DelDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        accountService.delete(dto.tid, dto.id!!)
        return CommonResult()
    }

    @Explain("账号列表")
    @RequestMapping("/list")
    open fun allChannels(page: TenantPage, @Valid dto: ListDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val accounts = accountService.list(dto.tid, dto.name, dto.filter, page.pageIndex, page.pageSize)
        return CommonResult(MapConverter(MapConverter.Converter<Account> { item, map ->
            with(item) {
                map.set("id", id)
                        .set("name", name)
                        .set("number", number)
                        .set("appId", appId)
                        .set("remark", remark)
            }
        }).build(accounts), accounts.getPage())
    }

    @Explain("修改账号信息")
    @RequiresPermissions("wx_account.update")
    @RequestMapping("/update")
    open fun modifyChannel(@Valid dto: UpdateDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        accountService.update(dto.tid, dto.id!!, dto.name, dto.token, dto.number, dto.appSecret, dto.aesKey, dto.remark)
        return CommonResult()
    }

    @Explain("账号详情")
    @RequiresPermissions("wx_account.update")
    @RequestMapping("/get")
    open fun getChannel(@Valid dto: GetDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val account = accountService.get(dto.tid, dto.id!!)
        var url = propertiesService.sysBaseurl + "/api/wechat/${dto.tid}/${dto.id}"
        return CommonResult(MapConverter(MapConverter.Converter<Account> { item, map ->
            with(item) {
                map.set("id", id)
                        .set("name", name)
                        .set("number", number)
                        .set("appId", appId)
                        .set("remark", remark)
                        .set("callback", url)
                        .set("webip", accountService.getWebIP())
            }
        }).build(account))
    }
}
