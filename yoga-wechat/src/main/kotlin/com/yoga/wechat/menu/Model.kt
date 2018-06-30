package com.yoga.wechat.menu

import com.yoga.core.data.BaseEnum
import com.yoga.core.interfaces.wechat.IdentityType
import me.chanjar.weixin.common.bean.menu.WxMenuButton
import me.chanjar.weixin.mp.bean.menu.WxMpMenu
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.*
import javax.validation.constraints.NotNull

enum class RuleGender(val type: String, val desc: String): BaseEnum<String> {
    all("", "全部"),
    male("1", "男"),
    female("2", "女");
    override fun getCode(): String = type
    override fun getName(): String = desc
}
enum class RuleOs(val type: String, val desc: String): BaseEnum<String> {
    all("", "全部"),
    ios("1", "iOS"),
    android("2", "Android"),
    other("3", "其他");
    override fun getCode(): String = type
    override fun getName(): String = desc
}
enum class RuleLanguage(val type: String, val desc: String): BaseEnum<String> {
    all("", "全部"),
    zh_CN("zh_CN", "简体中文"),
    zh_TW("zh_TW", "繁体中文"),
    zh_HK("zh_HK", "香港HK"),
    en("en", "英文"),
    id("id", "印尼"),
    ms("ms", "马来"),
    es("es", "西班牙"),
    ko("ko", "韩国"),
    it("it", "意大利"),
    jp("jp", "日本"),
    pl("pl", "波兰"),
    pt("pt", "葡萄牙0"),
    ru("ru", "俄国"),
    th("th", "泰文"),
    vi("vi", "越南"),
    ar("ar", "阿拉伯语"),
    hi("hi", "北印度"),
    he("he", "希伯来"),
    tr("tr", "土耳其"),
    de("de", "德语"),
    fr("fr", "法语");
    override fun getCode(): String = type
    override fun getName(): String = desc
}

enum class MenuType(val type: String, val desc: String): BaseEnum<String> {
    none("", "主菜单"),
    view("view", "跳转URL"),
    click("click", "点击推事件"),
    scancode_push("scancode_push", "扫码推事件"),
    scancode_waitmsg("scancode_waitmsg", "扫码带提示"),
    pic_sysphoto("pic_sysphoto", "弹出系统拍照发图"),
    pic_photo_or_album("pic_photo_or_album", "弹出拍照或者相册发图"),
    pic_weixin("pic_weixin", "弹出微信相册发图"),
    media_id("media_id", "下发消息"),
    view_limited("view_limited", "图文消息"),
    location_select("location_select", "地理位置选择"),
    miniprogram("miniprogram", "小程序");
    override fun getCode(): String {
        return type
    }
    override fun getName(): String {
        return desc
    }
    companion object {
        fun getEnum(type: String?): MenuType {
            if (type == null) return none
            enumValues<MenuType>().forEach {
                if (it.type == type) return@getEnum it
            }
            return none
        }
    }
}

inline fun <reified T: Enum<T>> getEnum(code: String?): T? {
    if (code == null) return null
    enumValues<T>().forEach {
        if (it.name == code) return@getEnum it
    }
    return null
}

@Entity
@Table(name = "wx_menu_entity")
data class MenuEntity(@Id var id: Long = 0,
                      @Column(name = "menu_id") var menuId: Long = 0,
                      @Enumerated(EnumType.STRING) @Column(name = "type") var type: MenuType = MenuType.none,
                      @Column(name = "sort") var sort: Int = 0,
                      @Column(name = "name") var name: String? = null,
                      @Column(name = "url") var url: String? = null,
                      @Column(name = "media_id") var mediaId: String? = null,
                      @Column(name = "app_id") var appid: String? = null,      //for miniprogram
                      @Column(name = "page_path") var pagePath: String? = null,   //for miniprogram
                      @Column(name = "parent_id") var parentId: Long = 0,
                      @Column(name = "raw_config") var rawConfig: String? = null,
                      @Column(name = "plugin_code") var pluginCode: String? = null,
                      @Enumerated(EnumType.STRING) @Column(name = "identity_type") var identityType: IdentityType? = null,
                      @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY) var children: List<MenuEntity>? = null) {
    constructor(button: WxMenuButton, menuId: Long, parentId: Long):
            this(0, menuId, MenuType.getEnum(button.type), 0, button.name, button.url, button.mediaId, button.appId, button.pagePath, parentId)
    constructor(button: WxMenuButton, menuId: Long):
            this(button, menuId, 0)
}
@Entity
@Table(name = "wx_menu")
data class Menu(@Id var id: Long = 0,
                @Column(name = "tenant_id") var tenantId: Long = 0,
                @Column(name = "account_id") var accountId: Long = 0,
                @Column(name = "is_default") var isDefault: Boolean = false,
                @Column(name = "name") var name: String? = null,
                @Column(name = "remark") var remark: String? = null,
                @Column(name = "wx_menu_id") var wxMenuId: String? = null,
                @Column(name = "tag_id") var tagId: String? = null,
                @field:Enumerated(EnumType.STRING) @Column(name = "gender") var gender: RuleGender? = null,
                @Column(name = "country") var country: String? = null,
                @Column(name = "province") var province: String? = null,
                @Column(name = "city") var city: String? = null,
                @field:Enumerated(EnumType.STRING) @Column(name = "client_os") var clientOS: RuleOs? = null,
                @field:Enumerated(EnumType.STRING) @Column(name = "language") var language: RuleLanguage? = null,
                @Transient var entities: List<MenuEntity>? = null) {
    constructor(id: Long, tenantId: Long, accountId: Long, isDefault: Boolean, menu: WxMpMenu.WxMpConditionalMenu):
            this(id, tenantId, accountId, isDefault, null, null, menu.menuId, menu.rule.tagId,
                    getEnum<RuleGender>(menu.rule.sex),
                    menu.rule.country, menu.rule.province, menu.rule.city,
                    getEnum<RuleOs>(menu.rule.clientPlatformType),
                    getEnum<RuleLanguage>(menu.rule.language))
    constructor(): this(0)
}

data class ApiMenuEntitiy(
        @field:NotNull(message = "菜单项类型不能为空") var type: MenuType? = MenuType.none,
        var sort: Int = 0,
        @field:NotEmpty(message = "菜单项名称不能为空") var name: String? = null,
        var url: String? = null,
        var mediaId: String? = null,
        var appId: String? = null,
        var pagePath: String? = null,
        var parentId: Long = 0,
        var rawConfig: String? = null,
        var pluginCode: String? = null,
        var children: List<ApiMenuEntitiy>? = null)
data class MenuUpdateBean(
        @field:NotNull(message = "菜单ID不能为空") var menuId: Long? = null,
        @field:NotNull(message = "菜单项不能为空") @field:NotEmpty(message = "菜单项不能为空") var entities: List<ApiMenuEntitiy>? = null)



