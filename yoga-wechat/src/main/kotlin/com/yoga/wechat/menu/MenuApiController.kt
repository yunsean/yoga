package com.yoga.wechat.menu

import com.yoga.core.annotation.Explain
import com.yoga.core.data.CommonResult
import com.yoga.core.exception.IllegalArgumentException
import com.yoga.core.interfaces.wechat.EventType
import com.yoga.user.basic.TenantDto
import com.yoga.wechat.actions.ActionsService
import com.yoga.wechat.base.WxBaseApiController
import org.apache.commons.collections.map.HashedMap
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Explain("WX菜单管理", module = "wx_menu")
@RestController
@RequestMapping("/api/wechat/account/menu")
open class MenuApiController @Autowired constructor(
        val menuService: MenuService,
        val actionsService: ActionsService) : WxBaseApiController() {

    @Explain("从公众号刷新")
    @RequiresPermissions("wx_menu.update")
    @RequestMapping("/refresh")
    open fun refresh(@Valid dto: RefreshDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        menuService.refresh(dto.tid, dto.accountId!!)
        return CommonResult()
    }

    @Explain("同步到公众号")
    @RequiresPermissions("wx_menu.publish")
    @RequestMapping("/publish")
    open fun publish(@Valid dto: PublishDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        menuService.publish(dto.tid, dto.accountId!!)
        return CommonResult()
    }

    @Explain("获取菜单条件")
    @RequiresPermissions("wx_menu.update")
    @RequestMapping("/get")
    open fun getMenu(@Valid dto: GetDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val menu = menuService.getMenu(dto.tid, dto.id!!)
        return CommonResult(menu)
    }

    @Explain("添加菜单")
    @RequiresPermissions("wx_menu.update")
    @RequestMapping("/add")
    open fun addMenu(@Valid dto: AddDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        with(dto) { menuService.addMenu(tid, accountId!!, isDefault, name!!, remark, tagId, gender, country, province, city, clientOS, language) }
        return CommonResult()
    }

    @Explain("修改菜单")
    @RequiresPermissions("wx_menu.update")
    @RequestMapping("/update")
    open fun updateMenu(@Valid dto: UpdateDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        with(dto) { menuService.updateMenu(tid, id!!, name, remark, tagId, gender, country, province, city, clientOS, language) }
        return CommonResult()
    }

    @Explain("删除菜单")
    @RequiresPermissions("wx_menu.update")
    @RequestMapping("/delete")
    open fun deleteMenu(@Valid dto: DeleteDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        menuService.deleteMenu(dto.tid, dto.id!!)
        return CommonResult()
    }

    @Explain("保存菜单项")
    @RequiresPermissions("wx_menu.update")
    @RequestMapping(value = "/save")
    open fun addEntities(@Valid @RequestBody bean: MenuUpdateBean, @Valid dto: TenantDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        menuService.updateMenu(dto.tid, bean.menuId!!, bean.entities!!)
        return CommonResult()
    }

    @Explain("编辑菜单项")
    @RequiresPermissions("wx_menu.update")
    @RequestMapping(value = "/edit")
    open fun updateEntity(@Valid dto: ToEntityEditDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        if (!dto.isdefault) {
            val entities = menuService.getEntities(dto.menuId ?: return CommonResult("非默认菜单的menuId不能为空"))
            val map = HashedMap()
            map.put("entities", entities)
            return CommonResult(map)
        } else if (dto.accountId != 0L) {
            val menu = menuService.defaultMenu(dto.tid, dto.accountId ?: return CommonResult("默认菜单的accountId不能为空"))
            return CommonResult(menu)
        }
        return CommonResult()
    }

    @Explain(exclude = true)
    @RequiresPermissions("wx_menu.update")
    @RequestMapping(value = "/actions")
    open fun actions(@Valid dto: TenantDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val actions = actionsService.actions(EventType.menu)
        return CommonResult(actions)
    }

    @Explain(exclude = true)
    @RequiresPermissions("wx_menu.update")
    @RequestMapping("/param")
    open fun trasParam(): CommonResult{
        return CommonResult()
    }
}

