package com.yoga.wechat.weixin

import com.yoga.core.data.BaseEnum
import javax.persistence.*

/** Created on 2017/7/25 **/

//微信推送事件的 event 类型
enum class EventType(val type: String, val desc: String) : BaseEnum<String> {
    CLICK("CLICK", "自定义菜单事件"),
    VIEW("VIEW", "点击菜单跳转链接时的事件"),
    SCAN("SCAN", "扫描带参数二维码事件"),
    LOCATION("LOCATION", "上报地理位置事件"),
    subscribe("subscribe", "关注事件"),
    unsubscribe("unsubscribe", "取消关注事件");

    override fun getCode(): String = type
    override fun getName(): String = desc

    companion object {
        fun getEnum(type: String?): EventType {
            if (type == null) return EventType.CLICK
            enumValues<EventType>().forEach {
                if (it.type == type) return@getEnum it
            }
            return EventType.CLICK
        }
    }
}

enum class ActionType(val type: String, val desc: String) : BaseEnum<String> {
    html("html", "HTML文本"),
    text("text", "文本消息"),
    plugin("plugin", "任务插件");

    override fun getCode(): String = type
    override fun getName(): String = desc
}


@Entity
@Table(name = "wx_action")
data class Action(@Id var id: Long = 0,
                  @Column(name = "tenant_id") var tenantId: Long = 0,
                  @Column(name = "account_id") var accountId: Long = 0,
                  @Column(name = "event_key") var key: String = "",
                  @field:Enumerated(EnumType.STRING) @Column(name = "action") var actionType: ActionType = ActionType.text,
                  @Column(name = "plugin_code") var pluginCode: String? = null,
                  @Column(name = "params") var params: String? = null)







