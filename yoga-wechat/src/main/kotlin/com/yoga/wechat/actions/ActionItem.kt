package com.yoga.wechat.actions

import com.yoga.core.interfaces.wechat.EventType
import com.yoga.core.interfaces.wechat.WechatAction

data class ActionItem(
        val name: String,
        val code: String,
        val events: Set<EventType>,
        val needConfig: Boolean,
        val action: WechatAction)