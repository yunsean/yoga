package com.yoga.wechat.sequence

import com.yoga.core.data.BaseEnum

enum class SequenceNameEnum constructor(
        private val code: String,
        private val desc: String): BaseEnum<String> {

    SEQ_WX_ACCOUNT_ID("seq_wx_account_id", "账号Id"),
    SEQ_WX_MENU_ID("seq_wx_menu_id", "菜单Id"),
    SEQ_WX_MENU_ENTITY_ID("seq_wx_menu_entity_id", "菜单条目Id"),
    SEQ_WX_USER_ID("seq_wx_user_id", "微信用户Id"),
    SEQ_WX_REPLY_ID("seq_wx_reply_id", "微信自定义回复Id"),
    SEQ_WX_MATERIAL_ID("seq_wx_material_id", "微信素材Id"),
    SEQ_WX_MATERIAL_GROUP_ID("seq_wx_material_group_id", "微信素材组Id"),
    SEQ_WX_ARTICLE_ID("seq_wx_article_id", "微信图文素材Id"),
    SEQ_WX_ACTION_ID("seq_wx_action_id", "自定义EventKey事件Id"),
    SEQ_WX_OFFICE_ID("seq_wx_office_id", "微信法制地图机构Id"),
    SEQ_WX_SYNC_ID("seq_wx_sync_id", "微信用户同步Id");

    override fun getCode(): String {
        return code
    }
    override fun getName(): String {
        return desc
    }
}