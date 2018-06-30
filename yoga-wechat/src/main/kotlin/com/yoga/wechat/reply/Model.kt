package com.yoga.wechat.reply

import com.fasterxml.jackson.annotation.JsonIgnore
import com.yoga.core.data.BaseEnum
import javax.persistence.*


enum class Gender(val type: String, val desc: String) : BaseEnum<String> {
    unknown("unknown", "全部"),
    male("male", "男"),
    female("female", "女");

    override fun getCode(): String = type
    override fun getName(): String = desc

    companion object {
        fun getEnum(type: String?): Gender {
            if (type == null) return Gender.unknown
            enumValues<Gender>().forEach {
                if (it.type == type) return@getEnum it
            }
            return Gender.unknown
        }
    }
}

enum class Event(val type: String, val desc: String) : BaseEnum<String> {
    subscribe("subscribe", "订阅公众号"),
    unsubscribe("unsubscribe", "取消订阅"),
    keyword("keyword", "关键字"),
    text("text", "文字消息"),
    image("image", "图片消息"),
    voice("voice", "语音消息"),
    video("video", "视频消息"),
    shortvideo("shortvideo", "小视频消息"),
    location("location", "地理位置消息"),
    common("common", "缺省回复");

    override fun getCode(): String = type
    override fun getName(): String = desc
}

enum class MessageType(val type: String, val desc: String) : BaseEnum<String> {
    image("image", "图片消息"),
    voice("voice", "语音消息"),
    video("video", "视频消息"),
    music("music", "音乐消息"),
    news("news", "图文消息"),
    text("text", "文字消息"),
    plugin("plugin", "跳转任务");

    override fun getCode(): String = type
    override fun getName(): String = desc
}

inline fun <reified T : Enum<T>> getEnum(code: String?): T? {
    if (code == null) return null
    enumValues<T>().forEach {
        if (it.name == code) return@getEnum it
    }
    return null
}

@Entity(name = "wechatReply")
@Table(name = "wx_reply")
open class Reply(@Id var id: Long = 0,
                 @Column(name = "tenant_id") var tenantId: Long = 0,
                 @Column(name = "account_id") var accountId: Long = 0,
                 @Enumerated(EnumType.STRING) @Column(name = "event") var event: Event = Event.keyword,
                 @Column(name = "tag") var tag: Int = 0,
                 @Enumerated(EnumType.STRING) @Column(name = "gender") var gender: Gender = Gender.unknown,
                 @Column(name = "name") var name: String = "",
                 @Column(name = "keyword") var keyword: String? = null,
                 @Enumerated(EnumType.STRING) @Column(name = "message_type") var messageType: MessageType = MessageType.text,
                 @Column(name = "text") var text: String? = null,
                 @Column(name = "media_id") var mediaId: String? = null,
                 @Column(name = "media_name") var mediaName: String? = null,
                 @Column(name = "title") var title: String? = null,
                 @Column(name = "description") var description: String? = null,
                 @Column(name = "music_url") var musicUrl: String? = null,
                 @Column(name = "hq_music_url") var hqMusicUrl: String? = null,
                 @Column(name = "plugin_code") var pluginCode: String = "",
                 @Column(name = "plugin_config") var pluginConfig: String = "") {
    @JsonIgnore fun keywords(): List<String> {
        return keyword?.let { it.split(",") } ?: listOf()
    }
}