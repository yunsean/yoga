package com.yoga.wechat.util

import com.yoga.core.data.BaseEnum
import java.util.*
import javax.persistence.*

enum class WxSyncAction(val type: String, val desc: String) : BaseEnum<String> {
    user("user", "用户列表"),
    image("image", "图片素材列表"),
    video("video", "视频素材列表"),
    voice("voice", "音频素材列表"),
    thumb("thumb", "头图素材列表"),
    file("file", "文件素材列表"),
    news("news", "图文素材列表");

    override fun getCode(): String = type
    override fun getName(): String = desc
}

@Entity
@Table(name = "wx_sync")
data class WxSync(@Id var id: Long = 0,
                  @Column(name = "tenant_id") var tenantId: Long = 0,
                  @Column(name = "account_id") var accountId: Long = 0,
                  @Column(name = "begin_time") var beginTime: Date = Date(),
                  @Column(name = "end_time") var endTime: Date? = null,
                  @Column(name = "finished") var finished: Boolean = false,
                  @Column(name = "notified") var notified: Boolean = false,
                  @Column(name = "actor") var actor: String = "",
                  @Column(name = "actor_id") var actorId: Long = 0,
                  @Enumerated(EnumType.STRING) @Column(name = "action") var action: WxSyncAction = WxSyncAction.user) {
    constructor() : this(0)
}

class WxArea() {
    var id: Long = 0
    var name: String = ""
    var children: List<WxArea>? = null
    var map: Map<Long, WxArea>? = null
}






