package com.yoga.wechat.menu

import com.yoga.core.exception.BusinessException
import com.yoga.core.interfaces.wechat.IdentityType
import com.yoga.core.interfaces.wechat.MenuEvent
import com.yoga.core.service.BaseService
import com.yoga.wechat.account.AccountRepository
import com.yoga.wechat.actions.ActionsService
import com.yoga.wechat.oauth2.OAuth2Service
import com.yoga.wechat.sequence.SequenceNameEnum
import com.yoga.wechat.weixin.ActionService
import com.yoga.wechat.weixin.ActionType
import com.yoga.wechat.weixin.WeixinServiceFactory
import me.chanjar.weixin.common.bean.menu.WxMenu
import me.chanjar.weixin.common.bean.menu.WxMenuButton
import me.chanjar.weixin.common.bean.menu.WxMenuRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
open class MenuService @Autowired constructor(
        val weixinServiceFactory: WeixinServiceFactory,
        val accountRepository: AccountRepository,
        val menuRepository: MenuRepository,
        val menuEntityRepository: MenuEntityRepository,
        val actionsService: ActionsService,
        val actionService: ActionService,
        var oAuth2Service: OAuth2Service) : BaseService() {

    open fun getMenu(tenantId: Long, menuId: Long): Menu {
        val menu = menuRepository.findOneByTenantIdAndId(tenantId, menuId)
        if (menu == null) throw BusinessException("菜单不存在")
        menu.entities = menuEntityRepository.findByMenuIdAndParentId(menu.id, 0L)
        return menu
    }

    open fun addMenu(tenantId: Long, accountId: Long, isDefault: Boolean, name: String, remark: String? = null, tagId: String? = null,
                     gender: RuleGender? = null, country: String? = null, province: String? = null, city: String? = null,
                     clientOS: RuleOs? = null, language: RuleLanguage? = null): Menu {
        val account = accountRepository.findOneByTenantIdAndId(tenantId, accountId)
        if (account == null) throw BusinessException("账号不存在")
        if (isDefault) {
            val menu = menuRepository.findFirstByTenantIdAndAccountIdAndIsDefault(tenantId, accountId, true)
            if (menu != null) throw BusinessException("已存在默认菜单")
        } else if (tagId.isNullOrBlank() && country.isNullOrBlank() &&
                (gender == null || gender == RuleGender.all) &&
                (clientOS == null || clientOS == RuleOs.all) &&
                (language == null || language == RuleLanguage.all)) {
            throw BusinessException("条件菜单至少需要制定一个条件")
        } else if ((!city.isNullOrBlank() && province.isNullOrBlank()) || (!province.isNullOrBlank() && country.isNullOrBlank())) {
            throw BusinessException("地域条件必须从上级开始指定")
        }
        val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MENU_ID)
        val menu = Menu(id, tenantId, accountId, isDefault, name, remark, null)
        menu.let {
            it.tagId = tagId
            it.gender = gender
            it.country = country
            it.province = province
            it.city = city
            it.clientOS = clientOS
            it.language = language
        }
        menuRepository.save(menu)
        return menu
    }

    open fun updateMenu(tenantId: Long, id: Long, name: String?, remark: String?, tagId: String?,
                        gender: RuleGender?, country: String?, province: String?, city: String?,
                        clientOS: RuleOs?, language: RuleLanguage?) {
        val menu = menuRepository.findOneByTenantIdAndId(tenantId, id)
        if (menu == null) throw BusinessException("菜单不存在")
        if (!name.isNullOrBlank()) menu.name = name
        if (!remark.isNullOrBlank()) menu.remark = remark
        menu.tagId = tagId
        menu.gender = gender
        menu.country = country
        menu.province = province
        menu.city = city
        menu.clientOS = clientOS
        menu.language = language
        menuRepository.save(menu)
    }

    open fun deleteMenu(tenantId: Long, id: Long) {
        val menu = menuRepository.findOneByTenantIdAndId(tenantId, id)
        if (menu == null) throw BusinessException("菜单不存在")
        menuRepository.delete(menu)
    }

    open fun getEntities(menuId: Long): List<MenuEntity>? {
        val entities = menuEntityRepository.findByMenuIdAndParentId(menuId, 0L)
        return entities
    }

    @Transactional
    open fun updateMenu(tenantId: Long, menuId: Long, apiEntities: List<ApiMenuEntitiy>) {
        val menu = menuRepository.findOne(menuId)
        if (menu == null || menu.tenantId != tenantId) throw BusinessException("该菜单不存在")
        if (apiEntities.isEmpty()){
            throw BusinessException("菜单不能为空")
        }
        val convert = {it: ApiMenuEntitiy, parentId: Long->
            if (!it.pluginCode.isNullOrEmpty()) {
                val action = actionsService.action(it.pluginCode!!)
                val event = action?.action?.menuEvent()
                if (action == null || event == null) throw BusinessException("未找到菜单对应的任务插件")
                val type = when (event) {
                    MenuEvent.view-> MenuType.view
                    MenuEvent.click-> MenuType.click
                    MenuEvent.scancode_push-> MenuType.scancode_push
                    MenuEvent.scancode_waitmsg-> MenuType.scancode_waitmsg
                    MenuEvent.pic_sysphoto-> MenuType.pic_sysphoto
                    MenuEvent.pic_photo_or_album-> MenuType.pic_photo_or_album
                    MenuEvent.pic_weixin-> MenuType.pic_weixin
                    MenuEvent.location_select-> MenuType.location_select
                }
                var url = it.url
                if (event == MenuEvent.view) {
                    url = it.rawConfig
                    if (url.isNullOrEmpty()) url = action.action.clickedUrl(tenantId, menu.accountId)
                }
                val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MENU_ENTITY_ID)
                MenuEntity(id, menuId, type, it.sort, it.name, url, it.mediaId, it.appId, it.pagePath, parentId, it.rawConfig, it.pluginCode, action.action.expectIdentity())
            } else {
                val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MENU_ENTITY_ID)
                MenuEntity(id, menuId, it.type!!, it.sort, it.name, it.url, it.mediaId, it.appId, it.pagePath, parentId, it.rawConfig, it.pluginCode)
            }
        }
        menuEntityRepository.deleteByMenuId(menuId)
        val entities = mutableListOf<MenuEntity>()
        apiEntities.forEach {
            if (it.children?.size ?: 0 > 0) {
                it.type = MenuType.none
                it.url = null
            }
            val menuEntity = convert(it, 0L)
            it.children?.forEach {
                entities.add(convert(it, menuEntity.id))
            }
            entities.add(menuEntity)
        }
        menuEntityRepository.save(entities)
    }

    open fun defaultMenu(tenantId: Long, accountId: Long): Menu {
        val menu = menuRepository.findFirstByTenantIdAndAccountIdAndIsDefault(tenantId, accountId, true)
        menu?.let { it.entities = menuEntityRepository.findByMenuIdAndParentId(it.id, 0L) }
        if (menu == null) return addMenu(tenantId, accountId, true, "Default Menu")
        else return menu
    }

    open fun conditionalMenus(tenantId: Long, accountId: Long, returnEntity: Boolean): List<Menu> {
        val menus = menuRepository.findByTenantIdAndAccountId(tenantId, accountId)
        var firstDefault = true;
        return menus?.filter {
            if (it.isDefault && firstDefault) {
                firstDefault = false;
                false;
            } else if (returnEntity) {
                it.entities = menuEntityRepository.findByMenuIdAndParentId(it.id, 0L)
                true
            } else {
                true
            }
        } ?: listOf()
    }

    @Transactional
    open fun refresh(tenantId: Long, accountId: Long) {
        val service = weixinServiceFactory.getService(accountId)
        val wxMenu = service.menuService.menuGet()
        val oldMenus = menuRepository.findByTenantIdAndAccountId(tenantId, accountId)
        val newMenus = mutableListOf<Menu>()
        val newMenuEntities = mutableListOf<MenuEntity>()

        wxMenu?.menu?.let {
            val old = oldMenus?.find { thiz -> thiz.wxMenuId == null || thiz.wxMenuId == "" }
            val menu = Menu()
            menu.isDefault = true
            menu.id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MENU_ID)
            menu.tenantId = tenantId
            menu.accountId = accountId
            menu.name = old?.name ?: "默认菜单"
            menu.remark = old?.remark
            for (button in it.buttons) {
                val entity = MenuEntity(button, menu.id)
                entity.id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MENU_ENTITY_ID)
                newMenuEntities.add(entity)
                for (child in button.subButtons) {
                    val sub = MenuEntity(child, menu.id, entity.id)
                    sub.id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MENU_ENTITY_ID)
                    newMenuEntities.add(sub)
                }
            }
            newMenus.add(menu)
        }
        wxMenu?.conditionalMenu?.forEach {
            val old = oldMenus?.find { thiz -> thiz.wxMenuId == it.menuId }
            val id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MENU_ID)
            val menu = Menu(id, tenantId, accountId, false, it)
            menu.name = old?.name ?: ""
            menu.remark = old?.remark
            it.buttons.forEach {
                val entity = MenuEntity(it, menu.id)
                entity.id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MENU_ENTITY_ID)
                newMenuEntities.add(entity)
                for (child in it.subButtons) {
                    val sub = MenuEntity(child, menu.id, entity.id)
                    sub.id = sequenceService.getNextValue(SequenceNameEnum.SEQ_WX_MENU_ENTITY_ID)
                    newMenuEntities.add(sub)
                }
            }
            newMenus.add(menu)
        }

        menuEntityRepository.deleteByTenantIdAndAccountId(tenantId, accountId)
        menuRepository.deleteByTenantIdAndAccountId(tenantId, accountId)
        menuRepository.save(newMenus)
        menuEntityRepository.save(newMenuEntities)
    }

    @Transactional
    open fun publish(tenantId: Long, accountId: Long) {
        val localMenus = menuRepository.findByTenantIdAndAccountId(tenantId, accountId)
        if (localMenus == null || localMenus.size < 1) throw BusinessException("请先配置菜单项")
        val defaultMenu = localMenus.find { it.isDefault }
        val conditionalMenus = localMenus.filter { it !== defaultMenu }
        if (defaultMenu == null) throw BusinessException("未找到默认菜单")
        localMenus.forEach { it.entities = menuEntityRepository.findByMenuIdAndParentId(it.id, 0L) }
        if (defaultMenu.entities == null || defaultMenu.entities!!.size < 1) throw BusinessException("默认菜单未配置任何菜单项")

        val service = weixinServiceFactory.getService(accountId)
        service.menuService.menuDelete()

        actionService.deleteByAccountId(accountId)
        var wxMenu = createMenu(tenantId, accountId, defaultMenu)
        try {
            service.menuService.menuCreate(wxMenu)
            conditionalMenus.let {
                it.forEach {
                    if (it.entities != null && it.entities!!.size > 0) {
                        wxMenu = createMenu(tenantId, accountId, it)
                        it.wxMenuId = service.menuService.menuCreate(wxMenu)
                    }
                }
                menuRepository.save(conditionalMenus)
            }
        } catch (ex: Exception) {
            throw BusinessException(ex.message)
        }
    }

    private fun createMenu(tenantId: Long, accountId: Long, menu: Menu): WxMenu {
        var wxMenu = WxMenu()
        menu.apply {
            wxMenu.buttons = mutableListOf()
            entities?.forEach { wxMenu.buttons.add(createButton(tenantId, accountId, it)) }
            if (!tagId.isNullOrBlank() || (gender != null && gender != RuleGender.all) ||
                    !country.isNullOrBlank() || (clientOS != null && clientOS != RuleOs.all) ||
                    (language != null && language != RuleLanguage.all)) {
                wxMenu.matchRule = WxMenuRule()
                wxMenu.matchRule.let {
                    it.tagId = this.tagId
                    it.sex = this.gender?.type
                    it.country = this.country
                    it.province = this.province
                    it.city = this.city
                    it.clientPlatformType = this.clientOS?.type
                    it.language = this.language?.type
                }
            }
        }
        return wxMenu
    }

    private fun createButton(tenantId: Long, accountId: Long, entity: MenuEntity): WxMenuButton {
        val button = WxMenuButton()
        entity.apply {
            button.type = type.type
            button.name = name
            if (!url.isNullOrEmpty()) {
                button.url = if (identityType == IdentityType.openId) oAuth2Service.getOpenIdUrl(accountId, url)
                else if (identityType == IdentityType.userInfo) oAuth2Service.getUserInfoUrl(accountId, url)
                else url
            }
            button.mediaId = mediaId
            button.key = addAction(tenantId, accountId, this)
            button.appId = appid
            button.pagePath = pagePath
            children?.sortedBy { sort }?.forEach {
                button.subButtons.add(createButton(tenantId, accountId, it))
            }
        }
        return button
    }

    open fun addAction(tenantId: Long, accountId: Long, entity: MenuEntity): String? {
        if (entity.type != MenuType.view && entity.type != MenuType.none &&
                entity.type != MenuType.media_id && entity.type != MenuType.view_limited) {
            if (entity.pluginCode.isNullOrEmpty())
                return actionService.add(tenantId, accountId, ActionType.text, null, entity.url)
            else
                return actionService.add(tenantId, accountId, ActionType.plugin, entity.pluginCode, entity.rawConfig)
        }
        return null
    }
}