package com.yoga.wechat.weixin.model

data class ImageEventMessage(
        var eventKey: String? = null,
        var imageCount: Int = 0,
        var totalCount: Int = 0)