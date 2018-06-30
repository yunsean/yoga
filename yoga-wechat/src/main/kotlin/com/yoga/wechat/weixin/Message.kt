package com.yoga.wechat.weixin

import me.chanjar.weixin.common.api.WxConsts
import java.io.Serializable


class Message(var type: String = WxConsts.XML_MSG_TEXT,
              var content: String? = null,      //for mediaId pluginConfig text thumbMediaId
              var pluginCode: String? = null,
              var openid: String? = null,
              var fromUser: String? = null,
              val title: String? = null,
              val description: String? = null,
              val musicUrl: String? = null,
              val hqMusicUrl: String? = null) : Serializable